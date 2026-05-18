package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.repository.UserSubscriptionRepository;
import by.alexeysavchic.beer_pet_project.repository.WaveRepository;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CreateSubscriptionsOrders {
    private final UserSubscriptionRepository userSubscriptionRepository;

    private final WaveRepository waveRepository;

    private final Logger logger = LogManager.getLogger(CreateSubscriptionsOrders.class);

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    @SchedulerLock(name = "CreateSubscriptionsOrders", lockAtMostFor = "20m", lockAtLeastFor = "10s")
    protected void createOrders() {
        Wave wave = waveRepository.findTopByStatus(WaveStatus.NEW);
        if (wave == null) {
            return;
        }
        List<UserSubscription> userSubscriptionList =
                userSubscriptionRepository.findUserSubscriptionByUnexpiredDateAndSubscription(wave.getTypeOfSubscription());
        if (userSubscriptionList.isEmpty()) {
            logger.info(wave.getTypeOfSubscription() + " subscribes is absent");
            return;
        }
        OrderSet orderSet = new OrderSet();
        for (UserSubscription userSubscription : userSubscriptionList) {
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.NEW);
            order.setSummaryPrice(BigDecimal.ZERO);
            switch (userSubscription.getSubscription().getSubscriptionType()) {
                case BEER_OF_THE_MONTH -> {
                    order.setOrderType(OrderType.BEER_OF_THE_MONTH);
                    orderSet.setOrderType(OrderType.BEER_OF_THE_MONTH);
                }
                case YOUR_FAVORITE_BEER -> {
                    order.setOrderType(OrderType.YOUR_FAVORITE_BEER);
                    orderSet.setOrderType(OrderType.BEER_OF_THE_MONTH);
                }
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
            orderSet.addOrder(order);
            wave.addOrder(order);
        }
        wave.setOrderSet(orderSet);
        wave.setStatus(WaveStatus.SUCCESSFUL);
        waveRepository.save(wave);
    }
}
