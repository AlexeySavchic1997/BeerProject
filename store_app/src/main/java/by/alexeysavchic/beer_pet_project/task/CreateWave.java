package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.repository.WaveRepository;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CreateWave {
    private final WaveRepository waveRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    @SchedulerLock(name = "CreateWave", lockAtMostFor = "5m", lockAtLeastFor = "10s")
    public void setWaveRepository() {
        List<TypeOfSubscription> allTypes = Arrays.stream(TypeOfSubscription.values()).toList();
        List<TypeOfSubscription> alreadyCreatedWaveTypes = waveRepository.findCreatedNewSubscriptions();
        allTypes.removeAll(alreadyCreatedWaveTypes);
        List<Wave> wavesForSave = new ArrayList<>();
        for (TypeOfSubscription type : allTypes) {
            wavesForSave.add(create(type));
        }
        waveRepository.saveAll(wavesForSave);
    }

    private Wave create(TypeOfSubscription type) {
        Year year = Year.now();
        Month month = LocalDateTime.now().getMonth();
        TypeOfSubscription typeOfSubscription = type;
        Wave wave = new Wave();
        wave.setName("wave_" + year + "_" + month.name() + "_" + typeOfSubscription);
        wave.setYear(year);
        wave.setMonth(month);
        wave.setTypeOfSubscription(type);
        wave.setStatus(WaveStatus.NEW);
        return wave;
    }
}
