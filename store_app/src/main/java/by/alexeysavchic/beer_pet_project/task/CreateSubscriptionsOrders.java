package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CreateSubscriptionsOrders {
    private final UserSubscriptionRepository userSubscriptionRepository;

    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    @SchedulerLock(name = "CreateSubscriptionsOrders", lockAtMostFor = "20m", lockAtLeastFor = "10s")
    protected void createOrders() {
        List<UserSubscription> userSubscriptionList =
                userSubscriptionRepository.findUserSubscriptionByUnexpiredDate();
        if (userSubscriptionList.isEmpty()) {
            return;
        }
        List<Order> orders = new ArrayList<>();
        for (UserSubscription userSubscription : userSubscriptionList) {
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.NEW);
            order.setSummaryPrice(BigDecimal.ZERO);
            switch (userSubscription.getSubscription().getSubscriptionType()) {
                case BEER_OF_THE_MONTH -> order.setOrderType(OrderType.BEER_OF_THE_MONTH);
                case YOUR_FAVORITE_BEER -> order.setOrderType(OrderType.YOUR_FAVORITE_BEER);
            }
            order.setUser(userSubscription.getUser());
            List<OrderItem> orderItems = new ArrayList<>();
            for (Beer beer : userSubscription.getBeers()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setPrice(BigDecimal.ZERO);
                orderItem.setQuantity(1);
                orderItem.setBeer(beer);
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
            order.setOrderItems(orderItems);
            orders.add(order);
        }
        orderRepository.saveAll(orders);
    }
}
