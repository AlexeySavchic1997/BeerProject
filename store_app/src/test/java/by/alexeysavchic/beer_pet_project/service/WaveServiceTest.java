package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.repository.WaveRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.WaveServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WaveServiceTest {

    @Mock
    private WaveRepository waveRepository;

    @InjectMocks
    private WaveServiceImpl waveService;

    @Captor
    private ArgumentCaptor<List<Wave>> waveListCaptor;

    @Nested
    class createWavesTests {

        @Test
        void createsAllWavesWhenNoneExist() {
            when(waveRepository.findCreatedNewSubscriptions()).thenReturn(new ArrayList<>());

            waveService.createWaves();

            verify(waveRepository, times(1)).saveAll(waveListCaptor.capture());
            List<Wave> savedWaves = waveListCaptor.getValue();

            int expectedSize = TypeOfSubscription.values().length;
            assertEquals(expectedSize, savedWaves.size());

            Wave firstWave = savedWaves.get(0);
            Year currentYear = Year.now();
            Month currentMonth = LocalDateTime.now().getMonth();

            assertEquals(WaveStatus.NEW, firstWave.getStatus());
            assertEquals(currentYear, firstWave.getYear());
            assertEquals(currentMonth, firstWave.getMonth());

            String expectedName = "wave_" + currentYear + "_" + currentMonth.name() + "_" + firstWave.getTypeOfSubscription();
            assertEquals(expectedName, firstWave.getName());
        }

        @Test
        void createsOnlyMissingWavesWhenSomeExist() {
            List<TypeOfSubscription> existingWaves = new ArrayList<>(List.of(TypeOfSubscription.BEER_OF_THE_MONTH));
            when(waveRepository.findCreatedNewSubscriptions()).thenReturn(existingWaves);

            waveService.createWaves();

            verify(waveRepository, times(1)).saveAll(waveListCaptor.capture());
            List<Wave> savedWaves = waveListCaptor.getValue();

            int expectedSize = TypeOfSubscription.values().length - 1;
            assertEquals(expectedSize, savedWaves.size());

            boolean containsBeerOfTheMonth = savedWaves.stream()
                    .anyMatch(wave -> wave.getTypeOfSubscription() == TypeOfSubscription.BEER_OF_THE_MONTH);
            assertFalse(containsBeerOfTheMonth);
        }

        @Test
        void doesNotCreateAnyWavesIfAllExist() {
            List<TypeOfSubscription> allWaves = new ArrayList<>(Arrays.asList(TypeOfSubscription.values()));
            when(waveRepository.findCreatedNewSubscriptions()).thenReturn(allWaves);

            waveService.createWaves();

            verify(waveRepository, times(1)).saveAll(waveListCaptor.capture());
            List<Wave> savedWaves = waveListCaptor.getValue();

            assertTrue(savedWaves.isEmpty());
        }
    }
}