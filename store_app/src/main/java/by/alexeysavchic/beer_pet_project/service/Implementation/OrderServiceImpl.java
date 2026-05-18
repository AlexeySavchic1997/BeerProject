package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.exception.WarehouseUpdateServerException;
import by.alexeysavchic.beer_pet_project.mapper.OrderMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderSetRepository;
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

    private final OrderSetRepository orderSetRepository;

    private final ClientService clientService;

    private final SecurityContextService securityContextService;

    private final EmailService emailService;

    private final OrderMapper orderMapper;

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public GetOrderResponse createOrder(@Valid CreateOrderRequest request) {
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
    public void processingSubscriptionOrders(OrderType type) {
        OrderSet orderSet = orderSetRepository.findOrderSetByOrderType(type);
        List<Order> orders = orderSet.getOrders();
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

