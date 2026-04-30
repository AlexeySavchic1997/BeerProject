package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.response.OrderResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.exception.WarehouseUpdateServerException;
import by.alexeysavchic.beer_pet_project.mapper.OrderMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.ClientService;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import warehouse_api.UnpassedOrderResponse;

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

    private final ClientService clientService;

    private final SecurityContextService securityContextService;

    private final EmailService emailService;

    private final OrderMapper orderMapper;

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public OrderResponse createOrder(@Valid CreateOrderRequest request) {
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
        try {
            List<UnpassedOrderResponse> unpassedOrders = clientService.updateWarehouseInfo(request.getCart());
            if (unpassedOrders.isEmpty()) {
                order.setStatus(OrderStatus.PROCESSING);
                emailService.confirmOrderEmail(order.getOrderItems(), order.getSummaryPrice(), user);
                logger.info("user: " + user.getUsername() + " created order successfully");
            } else {
                order.setStatus(OrderStatus.INSUFFICIENT_INVENTORY);
                Map<String, Integer> unpassedOrdersMap = new HashMap<>();
                for (UnpassedOrderResponse unpassedOrder : unpassedOrders) {
                    unpassedOrdersMap.put(beerMap.get(unpassedOrder.getSku()).getName(), unpassedOrder.getAmount());
                }
                logger.info("user: " + user.getUsername() + " insufficient inventory for order");
                emailService.insufficientInventoryOrderEmail(unpassedOrdersMap, user);
            }
        } catch (WarehouseUpdateServerException e) {
            logger.error(e.getMessage());
        } finally {
            orderRepository.save(order);
            return orderMapper.orderToOrderResponse(order);
            //should we return order in any case? I added order status in response DTO, but I don't think that is good decision
        }
    }
}
