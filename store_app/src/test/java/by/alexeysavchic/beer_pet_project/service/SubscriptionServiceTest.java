package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.CreateSubscriptionRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetSubscriptionsResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Subscription;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.mapper.SubscriptionMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.SubscriptionRepository;
import by.alexeysavchic.beer_pet_project.repository.UserSubscriptionRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Implementation.SubscriptionServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private SecurityContextService securityContextService;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Captor
    private ArgumentCaptor<UserSubscription> userSubscriptionCaptor;

    @Nested
    class getSubscriptionsTests {

        @Test
        void successfulGetSubscriptionsTest() {
            // Подготовка
            Subscription subscription = new Subscription();
            List<Subscription> subscriptions = List.of(subscription);

            GetSubscriptionsResponse responseDto = new GetSubscriptionsResponse();
            List<GetSubscriptionsResponse> responseDtos = List.of(responseDto);

            when(subscriptionRepository.findAll()).thenReturn(subscriptions);
            when(subscriptionMapper.listSubscriptionToListGetSubscriptionsResponse(subscriptions)).thenReturn(responseDtos);

            // Выполнение
            List<GetSubscriptionsResponse> result = subscriptionService.getSubscriptions();

            // Проверки
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(subscriptionRepository, times(1)).findAll();
            verify(subscriptionMapper, times(1)).listSubscriptionToListGetSubscriptionsResponse(subscriptions);
        }
    }

    @Nested
    class createUserSubscriptionTests {

        @Test
        void successfulCreateUserSubscriptionTest() {
            // Подготовка
            CreateSubscriptionRequest request = new CreateSubscriptionRequest();
            request.setTypeOfSubscription(TypeOfSubscription.BEER_OF_THE_MONTH);
            request.setDurationMonths(6); // Подписка на 6 месяцев
            request.setSkuList(List.of("BEER-1", "BEER-2"));

            Subscription subscription = new Subscription();
            subscription.setSubscriptionType(TypeOfSubscription.BEER_OF_THE_MONTH);

            User user = new User();
            user.setId(1L);

            Beer beer1 = new Beer();
            beer1.setSku("BEER-1");
            Beer beer2 = new Beer();
            beer2.setSku("BEER-2");
            List<Beer> beers = List.of(beer1, beer2);

            when(subscriptionRepository.findBySubscriptionType(TypeOfSubscription.BEER_OF_THE_MONTH)).thenReturn(subscription);
            when(securityContextService.getCurrentUser()).thenReturn(user);
            when(beerRepository.findAllBySku(List.of("BEER-1", "BEER-2"))).thenReturn(beers);

            subscriptionService.createUserSubscription(request);

            verify(userSubscriptionRepository, times(1)).save(userSubscriptionCaptor.capture());
            UserSubscription savedUserSubscription = userSubscriptionCaptor.getValue();

            assertNotNull(savedUserSubscription);
            assertEquals(subscription, savedUserSubscription.getSubscription());
            assertEquals(user, savedUserSubscription.getUser());
            assertEquals(2, savedUserSubscription.getBeers().size());

            assertNotNull(savedUserSubscription.getSubscribeDate());
            assertNotNull(savedUserSubscription.getTimeOfExpiration());

            long monthsBetween = ChronoUnit.MONTHS.between(
                    savedUserSubscription.getSubscribeDate(),
                    savedUserSubscription.getTimeOfExpiration()
            );
            assertEquals(6, monthsBetween);
        }
    }
}