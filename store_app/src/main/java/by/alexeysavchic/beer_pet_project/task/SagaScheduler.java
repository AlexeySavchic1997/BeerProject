package by.alexeysavchic.beer_pet_project.task;

import by.alexeysavchic.beer_pet_project.entity.Saga;
import by.alexeysavchic.beer_pet_project.repository.SagaRepository;
import by.alexeysavchic.beer_pet_project.worker.SagaWorker;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SagaScheduler
{
    private final SagaRepository sagaRepository;

    private final SagaWorker worker;

    @Scheduled(fixedDelay = 120000)
    @SchedulerLock(name = "SagaScheduler", lockAtMostFor = "2m", lockAtLeastFor = "10s")
    protected void manageSaga()
    {
        LocalDateTime delay=LocalDateTime.now().minusMinutes(2);
        List<Saga> sagaList=sagaRepository.findAllNotCompletedSagas(delay);
        for (Saga saga: sagaList)
        {
            worker.HandleSaga(saga);
        }
    }
}
