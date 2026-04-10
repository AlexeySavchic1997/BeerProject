package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
    private final WarehouseService warehouseService;

    @Scheduled(fixedDelay = 300000)
    private void GetInformationFromWarehouseTask() {
        warehouseService.getUpdatedWarehouseInfo();
    }
}
