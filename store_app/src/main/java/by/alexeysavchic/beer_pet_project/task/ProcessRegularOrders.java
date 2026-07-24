package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.service.Interface.OrderSetService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ProcessRegularOrders {
    private final OrderSetService orderSetService;

    @Scheduled(cron = "0 0/5 * * * *")
    @SchedulerLock(name = "ProcessRegularOrders", lockAtMostFor = "1m", lockAtLeastFor = "10s")
    public void run() {
        orderSetService.createRegularOrderSet();
    }
}
