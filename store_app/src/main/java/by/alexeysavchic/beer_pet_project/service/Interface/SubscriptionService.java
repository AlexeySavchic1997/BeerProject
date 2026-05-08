package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CreateSubscriptionRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetSubscriptionsResponse;

import java.util.List;

public interface SubscriptionService {
    public List<GetSubscriptionsResponse> getSubscriptions();

    public void createUserSubscription(CreateSubscriptionRequest request);
}
