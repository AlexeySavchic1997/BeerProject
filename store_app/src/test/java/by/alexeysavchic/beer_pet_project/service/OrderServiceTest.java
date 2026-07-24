package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.Subscription;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.Gender;
import by.alexeysavchic.beer_pet_project.entity.enums.Location;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.mapper.OrderMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.UserSubscriptionRepository;
import by.alexeysavchic.beer_pet_project.repository.WaveRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Implementation.OrderServiceImpl;
import by.alexeysavchic.beer_pet_project.service.Interface.ClientService;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BeerRepository beerRepository;
    @Mock
    private WaveRepository waveRepository;
    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private SecurityContextService securityContextService;
    @Mock
    private EmailService emailService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<List<Order>> orderListCaptor;

    @Nested
    class createOrderTests {

        @Test
        void successfullyCreatesOrderWithCorrectCalculations() {

            User mockUser = new User();
            mockUser.setUserGender(Gender.MALE);
            mockUser.setUserLocation(Location.BLR);
            when(securityContextService.getCurrentUser()).thenReturn(mockUser);

            OrderItemRequest item1 = new OrderItemRequest();
            item1.setSku("BEER-1");
            item1.setAmount(2);

            OrderItemRequest item2 = new OrderItemRequest();
            item2.setSku("BEER-2");
            item2.setAmount(3);

            CreateOrderRequest request = new CreateOrderRequest();
            request.setCart(List.of(item1, item2));

            Beer beer1 = new Beer();
            beer1.setSku("BEER-1");
            beer1.setPrice(new BigDecimal("10.00"));

            Beer beer2 = new Beer();
            beer2.setSku("BEER-2");
            beer2.setPrice(new BigDecimal("5.00"));

            when(beerRepository.findAllBySku(List.of("BEER-1", "BEER-2"))).thenReturn(List.of(beer1, beer2));

            orderService.createOrder(request);

            verify(orderRepository, times(1)).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();

            assertNotNull(savedOrder);
            assertEquals(OrderStatus.NEW, savedOrder.getStatus());
            assertEquals(OrderType.REGULAR_ORDER, savedOrder.getOrderType());
            assertNotNull(savedOrder.getOrderDate());
            assertEquals(mockUser, savedOrder.getUser());
            assertEquals(Gender.MALE, savedOrder.getOrderGender());
            assertEquals(Location.BLR, savedOrder.getOrderLocation());

            assertEquals(new BigDecimal("35.00"), savedOrder.getSummaryPrice());

            List<OrderItem> items = savedOrder.getOrderItems();
            assertEquals(2, items.size());

            OrderItem savedItem1 = items.stream().filter(i -> i.getBeer().getSku().equals("BEER-1")).findFirst().get();
            assertEquals(2, savedItem1.getQuantity());
            assertEquals(new BigDecimal("20.00"), savedItem1.getPrice());
            assertEquals(savedOrder, savedItem1.getOrder()); // Проверка двунаправленной связи

            OrderItem savedItem2 = items.stream().filter(i -> i.getBeer().getSku().equals("BEER-2")).findFirst().get();
            assertEquals(3, savedItem2.getQuantity());
            assertEquals(new BigDecimal("15.00"), savedItem2.getPrice());
        }

        @Test
        void successfullyCreatesEmptyOrderWhenCartIsEmpty() {
            User mockUser = new User();
            when(securityContextService.getCurrentUser()).thenReturn(mockUser);

            CreateOrderRequest request = new CreateOrderRequest();
            request.setCart(List.of()); // Пустая корзина

            orderService.createOrder(request);

            verify(orderRepository).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();

            assertEquals(BigDecimal.ZERO, savedOrder.getSummaryPrice());
            assertEquals(0, savedOrder.getOrderItems().size());
            verify(beerRepository).findAllBySku(List.of());
        }
    }

    @Nested
    class saveOrdersFromSubscriptionsTests {

        @Test
        void returnsWhenNoNewWaves() {
            when(waveRepository.findTopByStatus(WaveStatus.NEW)).thenReturn(null);

            orderService.saveOrdersFromSubscriptions();

            verify(userSubscriptionRepository, never()).findUserSubscriptionByUnexpiredDateAndSubscription(any());
            verify(orderRepository, never()).saveAll(any());
        }

        @Test
        void returnsWhenNoSubscriptionsFound() {
            Wave wave = new Wave();
            wave.setTypeOfSubscription(TypeOfSubscription.BEER_OF_THE_MONTH);

            when(waveRepository.findTopByStatus(WaveStatus.NEW)).thenReturn(wave);
            when(userSubscriptionRepository.findUserSubscriptionByUnexpiredDateAndSubscription(any())).thenReturn(new ArrayList<>());

            orderService.saveOrdersFromSubscriptions();

            verify(orderRepository, never()).saveAll(any());
        }

        @Test
        void successfullySavesOrdersAndUpdatesWaveStatus() {
            Subscription subscription = new Subscription();
            subscription.setSubscriptionType(TypeOfSubscription.BEER_OF_THE_MONTH);

            Wave wave = new Wave();
            wave.setStatus(WaveStatus.NEW);
            wave.setTypeOfSubscription(TypeOfSubscription.BEER_OF_THE_MONTH);

            User user = new User();
            Beer beer = new Beer();
            beer.setPrice(new BigDecimal("5.00"));

            UserSubscription userSubscription = new UserSubscription();
            userSubscription.setUser(user);
            userSubscription.setSubscription(subscription);
            userSubscription.setBeers(List.of(beer));

            when(waveRepository.findTopByStatus(WaveStatus.NEW)).thenReturn(wave);
            when(userSubscriptionRepository.findUserSubscriptionByUnexpiredDateAndSubscription(TypeOfSubscription.BEER_OF_THE_MONTH))
                    .thenReturn(List.of(userSubscription));

            orderService.saveOrdersFromSubscriptions();

            assertEquals(WaveStatus.PROCESSED, wave.getStatus());

            verify(orderRepository, times(1)).saveAll(orderListCaptor.capture());
            List<Order> savedOrders = orderListCaptor.getValue();
            assertEquals(1, savedOrders.size());
            assertEquals(OrderStatus.NEW, savedOrders.get(0).getStatus());
            assertEquals(1, savedOrders.get(0).getOrderItems().size());
        }
    }
}
