package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.repository.WaveRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.WaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaveServiceImpl implements WaveService {
    private final WaveRepository waveRepository;

    @Override
    public void createWaves() {
        List<TypeOfSubscription> allTypes = Arrays.stream(TypeOfSubscription.values()).collect(Collectors.toList());
        List<TypeOfSubscription> alreadyCreatedWaveTypes = waveRepository.findCreatedNewSubscriptions();
        allTypes.removeAll(alreadyCreatedWaveTypes);
        List<Wave> wavesForSave = new ArrayList<>();
        for (TypeOfSubscription type : allTypes) {
            Year year = Year.now();
            Month month = LocalDateTime.now().getMonth();
            Wave wave = new Wave();
            wave.setName("wave_" + year + "_" + month.name() + "_" + type);
            wave.setYear(year);
            wave.setMonth(month);
            wave.setTypeOfSubscription(type);
            wave.setStatus(WaveStatus.NEW);
            wavesForSave.add(wave);
        }
        waveRepository.saveAll(wavesForSave);
    }
}
