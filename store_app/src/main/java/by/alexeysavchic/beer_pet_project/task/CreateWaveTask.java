package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.service.Interface.WaveService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CreateWaveTask {

    private final WaveService waveService;

    @Scheduled(cron = "0 0 0 1 * *")
    @SchedulerLock(name = "CreateWaveTask", lockAtMostFor = "5m", lockAtLeastFor = "10s")
    public void run() {
        waveService.createWaves();
    }
}
