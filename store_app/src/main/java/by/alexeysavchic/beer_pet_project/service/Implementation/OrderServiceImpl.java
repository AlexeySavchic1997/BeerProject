package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.exception.TypeOfSubscriptionIsAbsent;
import by.alexeysavchic.beer_pet_project.exception.WarehouseUpdateServerException;
import by.alexeysavchic.beer_pet_project.mapper.OrderMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.UserSubscriptionRepository;
import by.alexeysavchic.beer_pet_project.repository.WaveRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Implementation.messages.OrderMessages;
import by.alexeysavchic.beer_pet_project.service.Interface.ClientService;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import warehouse_api.UnpassedOrderResponse;
import warehouse_api.UnpassedOrderSubscription;
import warehouse_api.UpdateBySubscribeRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final BeerRepository beerRepository;

    private final WaveRepository waveRepository;

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final ClientService clientService;

    private final SecurityContextService securityContextService;

    private final EmailService emailService;

    private final OrderMapper orderMapper;

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public GetOrderResponse createOrder(CreateOrderRequest request) {
        LocalDateTime timeMark = LocalDateTime.now();
        User user = securityContextService.getCurrentUser();
        List<OrderItemRequest> cart = request.getCart();
        List<String> keyList = new ArrayList<>();
        for (OrderItemRequest item : cart) {
            keyList.add(item.getSku());
        }
        List<Beer> beerList = beerRepository.findAllBySku(keyList);
        Map<String, Beer> beerMap = new HashMap<>();
        for (Beer beer : beerList) {
            beerMap.put(beer.getSku(), beer);
        }

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setOrderType(OrderType.REGULAR_ORDER);
        BigDecimal summaryPrice = BigDecimal.ZERO;
        for (OrderItemRequest item : cart) {
            Beer beer = beerMap.get(item.getSku());
            Integer quantity = item.getAmount();
            BigDecimal price = beer.getPrice().multiply(new BigDecimal(quantity));
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(quantity);
            orderItem.setBeer(beer);
            orderItem.setPrice(price);
            orderItem.setOrder(order);
            summaryPrice = summaryPrice.add(price);
            order.getOrderItems().add(orderItem);
        }
        order.setSummaryPrice(summaryPrice);
        order.setUser(user);
        order.setOrderGender(user.getUserGender());
        order.setOrderLocation(user.getUserLocation());
        order.setOrderDate(timeMark);
        orderRepository.save(order);
        try {
            order.setStatus(OrderStatus.PROCESSING);
            List<UnpassedOrderResponse> unpassedOrders = clientService.updateWarehouseInfoByOrder(request.getCart());
            if (unpassedOrders.isEmpty()) {
                order.setStatus(OrderStatus.COMPLETED);
                emailService.confirmOrderEmail(order.getOrderItems(), order.getSummaryPrice(), user);
                logger.info(String.format(OrderMessages.processingOrder, order.getId()));
            } else {
                order.setStatus(OrderStatus.INSUFFICIENT_INVENTORY);
                Map<String, Integer> unpassedOrdersMap = new HashMap<>();
                for (UnpassedOrderResponse unpassedOrder : unpassedOrders) {
                    unpassedOrdersMap.put(beerMap.get(unpassedOrder.getSku()).getName(), unpassedOrder.getAmount());
                }
                logger.info(String.format(OrderMessages.insufficientInventory, order.getId()));
                emailService.insufficientInventoryOrderEmail(unpassedOrdersMap, user);
            }
        } catch (WarehouseUpdateServerException e) {
            order.setStatus(OrderStatus.CANCELLED);
            logger.error(e.getMessage());
        } finally {
            orderRepository.save(order);
        }
        return orderMapper.orderToOrderResponse(order);
    }

    @Override
    public void saveOrdersFromSubscriptions() {
        Wave wave = waveRepository.findTopByStatus(WaveStatus.NEW);
        if (wave == null) {
            logger.info("there is no waves for processing");
            return;
        }
        List<UserSubscription> userSubscriptionList =
                userSubscriptionRepository.findUserSubscriptionByUnexpiredDateAndSubscription(wave.getTypeOfSubscription());
        if (userSubscriptionList.isEmpty()) {
            logger.info(wave.getTypeOfSubscription() + " subscribes is absent");
            return;
        }
        OrderSet orderSet = new OrderSet();
        orderSet.setOrderSetStatus(OrderSetStatus.NEW);
        List<Order> orders = new ArrayList<>();
        for (UserSubscription userSubscription : userSubscriptionList) {
            Order order = new Order();
            try {
                order = createOrderFromSubscribe(userSubscription, orderSet, wave);
            } catch (RuntimeException e) {
                logger.error("cannot create order from subscription " + userSubscription.toString());
                wave.setStatus(WaveStatus.ERROR);
                continue;
            }
            orders.add(order);
        }
        if (wave.getStatus() != WaveStatus.ERROR) {
            wave.setStatus(WaveStatus.PROCESSED);
        }
        orderRepository.saveAll(orders);
    }

    private Order createOrderFromSubscribe(UserSubscription userSubscription, OrderSet orderSet, Wave wave) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setSummaryPrice(BigDecimal.ZERO);
        TypeOfSubscription type = userSubscription.getSubscription().getSubscriptionType();
        switch (type) {
            case BEER_OF_THE_MONTH -> {
                order.setOrderType(OrderType.BEER_OF_THE_MONTH);
                orderSet.setOrderType(OrderType.BEER_OF_THE_MONTH);
            }
            case FAVORITE_BEER -> {
                order.setOrderType(OrderType.FAVORITE_BEER);
                orderSet.setOrderType(OrderType.FAVORITE_BEER);
            }
            default -> {
                throw new TypeOfSubscriptionIsAbsent(type);
            }
        }
        User user = userSubscription.getUser();
        order.setUser(user);
        order.setOrderLocation(user.getUserLocation());
        order.setOrderGender(user.getUserGender());
        List<OrderItem> orderItems = new ArrayList<>();
        for (Beer beer : userSubscription.getBeers()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(beer.getPrice());
            orderItem.setQuantity(1);
            orderItem.setBeer(beer);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setWave(wave);
        order.setOrderSet(orderSet);
        orderSet.setOrderSetStatus(OrderSetStatus.READY_TO_SPLIT);
        return order;
    }

    @Override
    public void processingSubscriptionOrders(OrderType type) {
        List<Order> orders = orderRepository.findAllByOrderSetType(type);
        List<UpdateBySubscribeRequest> updateList = new ArrayList<>();
        Map<Long, Order> orderMap = new HashMap<>();
        for (Order order : orders) {
            order.setStatus(OrderStatus.PROCESSING);
            UpdateBySubscribeRequest update = orderMapper.orderToUpdateBySubscribeRequest(order);
            updateList.add(update);
            orderMap.put(order.getUser().getId(), order);
        }
        List<UnpassedOrderSubscription> unpassedOrderSubscriptions = clientService.updateWarehouseInfoBySubscription(updateList);
        for (UnpassedOrderSubscription unpassedOrder : unpassedOrderSubscriptions) {
            Order order = orderMap.get(unpassedOrder.getUserId());
            List<UnpassedOrderResponse> unpassedList = unpassedOrder.getUnpassedListList();
            if (unpassedOrder.getUnpassedListList().isEmpty()) {
                order.setStatus(OrderStatus.COMPLETED);
                emailService.confirmOrderEmail(order.getOrderItems(), order.getSummaryPrice(), order.getUser());
                logger.info(String.format(OrderMessages.processingOrder, order.getId()));
            } else {
                orderMap.get(unpassedOrder.getUserId()).setStatus(OrderStatus.INSUFFICIENT_INVENTORY);
                List<String> unpassedBeerSku = new ArrayList<>();
                for (UnpassedOrderResponse unpassedItem : unpassedList) {
                    unpassedBeerSku.add(unpassedItem.getSku());
                }
                List<String> unpassedBeerNames = beerRepository.findAllBeerNamesBySku(unpassedBeerSku);
                logger.info(String.format(OrderMessages.insufficientInventory, order.getId()));
                emailService.insufficientInventorySubscriptionEmail(unpassedBeerNames, order.getUser());
            }
        }
        orderRepository.saveAll(orders);
    }
}

