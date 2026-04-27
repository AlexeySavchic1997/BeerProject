package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Saga;

public interface OrderOrchestrationService {
    public void makeOrder(CartOrderRequest request);

    public void continueOrder(Saga saga);

    public void cancelSaga(Saga saga);
}
