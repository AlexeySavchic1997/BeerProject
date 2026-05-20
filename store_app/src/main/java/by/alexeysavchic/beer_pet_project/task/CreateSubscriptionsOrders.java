package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CreateSubscriptionsOrders {
    private final OrderService orderService;

    @Scheduled(cron = "0/30 * 2 * *")
    @SchedulerLock(name = "CreateSubscriptionsOrders", lockAtMostFor = "20m", lockAtLeastFor = "10s")
    protected void run() {
        orderService.createOrdersFromSubscriptions();
    }
}
