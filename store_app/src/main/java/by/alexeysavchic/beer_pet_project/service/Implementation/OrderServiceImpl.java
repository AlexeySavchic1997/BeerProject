package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.exception.GlobalExceptionHandler;
import by.alexeysavchic.beer_pet_project.exception.UpdatingWarehouseXmlError;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService
{
    private final OrderRepository orderRepository;

    private final BeerRepository beerRepository;

    private final WarehouseService warehouseService;

    private final SecurityContextService securityContextService;

    private final EmailService emailService;

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @Override
    public void createOrder(CartOrderRequest request)
    {
        LocalDateTime timeMark = LocalDateTime.now();
        User user=securityContextService.getCurrentUser();
       List<OrderItemRequest> cart=request.getCart();
       List<Long> keyList=new ArrayList<>();
       for(OrderItemRequest item:cart)
       {
           keyList.add(item.getId());
       }
       List<Beer> beerList=beerRepository.findAllById(keyList);
       Map<Long, Beer> beerMap = new HashMap<>();
       for (Beer beer:beerList)
       {
           beerMap.put(beer.getId(),beer);
       }

       Order order = new Order();
       order.setStatus(OrderStatus.PROCESSING);
       BigDecimal summaryPrice=BigDecimal.ZERO;
       for (OrderItemRequest item:cart)
       {
           Beer beer = beerMap.get(item.getId());
           Integer quantity=item.getAmount();
           BigDecimal price=beer.getPrice().multiply(new BigDecimal(quantity));
           OrderItem orderItem=new OrderItem();
           orderItem.setQuantity(quantity);
           orderItem.setBeer(beer);
           orderItem.setPrice(price);
           orderItem.setOrder(order);
           summaryPrice=summaryPrice.add(price);
           order.getOrderItems().add(orderItem);
       }
       order.setSummaryPrice(summaryPrice);
       order.setUser(user);
       order.setOrderDate(timeMark);
       orderRepository.save(order);
       try {
           warehouseService.updateWarehouseInfo(request,timeMark);
           order.setStatus(OrderStatus.SUCCESSFUL);
           orderRepository.save(order);
           emailService.sendEmail(order.getOrderItems(), order.getSummaryPrice(), user);
       }
       catch (UpdatingWarehouseXmlError e)
       {
           logger.error(e.getMessage());
           order.setStatus(OrderStatus.CANCELED);
           orderRepository.save(order);
       }
    }
}
