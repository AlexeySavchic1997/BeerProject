package by.alexeysavchic.beer_pet_project.worker;

import by.alexeysavchic.beer_pet_project.entity.Saga;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class SagaWorker
{
    private final OrderOrchestrationService orchestrationService;

    @Async
    public void HandleSaga(Saga saga)
    {
        if (Duration.between(saga.getCreatedAt(), saga.getUpdatedAt()).toMinutes()<20)
        {
            orchestrationService.continueOrder(saga);
        }
        else
        {
            orchestrationService.cancelSaga(saga);
        }
    }

}
