package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CreateSubscriptionRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetSubscriptionsResponse;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.mapper.SubscriptionMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.SubscriptionRepository;
import by.alexeysavchic.beer_pet_project.repository.UserSubscriptionRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final BeerRepository beerRepository;

    private final SecurityContextService securityContextService;

    private final SubscriptionMapper subscriptionMapper;


    @Override
    public List<GetSubscriptionsResponse> getSubscriptions() {
        return subscriptionMapper.listSubscriptionToListGetSubscriptionsResponse(subscriptionRepository.findAll());
    }

    @Override
    public void createUserSubscription(@Valid CreateSubscriptionRequest request) {
        LocalDateTime timeOfCreation = LocalDateTime.now();
        LocalDateTime timeOfExpiration = timeOfCreation.plusMonths(request.getDurationMonths());

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setSubscription(subscriptionRepository.findBySubscriptionType(request.getTypeOfSubscription()));
        userSubscription.setUser(securityContextService.getCurrentUser());
        userSubscription.setSubscribeDate(timeOfCreation);
        userSubscription.setTimeOfExpiration(timeOfExpiration);
        userSubscription.setBeers(beerRepository.findAllBySku(request.getSkuList()));

        userSubscriptionRepository.save(userSubscription);
    }
}
