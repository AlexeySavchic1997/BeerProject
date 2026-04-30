package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class GetInformationFromWarehouse {
    private final WarehouseService warehouseService;

    @Scheduled(fixedDelay = 300000)
    @SchedulerLock(name = "GetInformationFromWarehouse", lockAtMostFor = "5m", lockAtLeastFor = "10s")
    protected void GetInformation() {
        warehouseService.getUpdatedWarehouseInfo();
    }
}
