package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.Subscription;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.UserSubscription;
import by.alexeysavchic.beer_pet_project.entity.Wave;
import by.alexeysavchic.beer_pet_project.entity.enums.Gender;
import by.alexeysavchic.beer_pet_project.entity.enums.Location;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import by.alexeysavchic.beer_pet_project.exception.WarehouseUpdateServerException;
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
import warehouse_api.UnpassedOrderResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
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
        void successfulCreateOrderTest() {
            User user = new User();
            user.setUserGender(Gender.MALE);
            user.setUserLocation(Location.BLR);

            OrderItemRequest itemRequest = new OrderItemRequest("BEER-1", 2);
            CreateOrderRequest request = new CreateOrderRequest(List.of(itemRequest));

            Beer beer = new Beer();
            beer.setSku("BEER-1");
            beer.setPrice(new BigDecimal("10.00"));
            beer.setName("Guinness");

            when(securityContextService.getCurrentUser()).thenReturn(user);
            when(beerRepository.findAllBySku(List.of("BEER-1"))).thenReturn(List.of(beer));

            when(clientService.updateWarehouseInfoByOrder(anyList())).thenReturn(new ArrayList<>());

            GetOrderResponse response = new GetOrderResponse();
            when(orderMapper.orderToOrderResponse(any(Order.class))).thenReturn(response);

            GetOrderResponse result = orderService.createOrder(request);

            assertNotNull(result);

            verify(orderRepository, times(2)).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue(); // Берем последнее сохранение (из finally)

            assertEquals(OrderStatus.COMPLETED, savedOrder.getStatus());
            assertEquals(new BigDecimal("20.00"), savedOrder.getSummaryPrice()); // 2 шт * 10.00

            verify(emailService, times(1)).confirmOrderEmail(anyList(), eq(new BigDecimal("20.00")), eq(user));
            verify(emailService, never()).insufficientInventoryOrderEmail(any(), any());
        }

        @Test
        void createsOrderWithInsufficientInventoryTest() {
            User user = new User();
            OrderItemRequest itemRequest = new OrderItemRequest("BEER-1", 5);
            CreateOrderRequest request = new CreateOrderRequest(List.of(itemRequest));

            Beer beer = new Beer();
            beer.setSku("BEER-1");
            beer.setPrice(new BigDecimal("10.00"));
            beer.setName("Guinness");


            when(securityContextService.getCurrentUser()).thenReturn(user);
            when(beerRepository.findAllBySku(List.of("BEER-1"))).thenReturn(List.of(beer));

            UnpassedOrderResponse unpassed = UnpassedOrderResponse.newBuilder().setSku("BEER-1").setAmount(3).build();
            when(clientService.updateWarehouseInfoByOrder(anyList())).thenReturn(List.of(unpassed));

            orderService.createOrder(request);

            verify(orderRepository, times(2)).save(orderCaptor.capture());
            assertEquals(OrderStatus.INSUFFICIENT_INVENTORY, orderCaptor.getValue().getStatus());

            verify(emailService, times(1)).insufficientInventoryOrderEmail(anyMap(), eq(user));
            verify(emailService, never()).confirmOrderEmail(any(), any(), any());
        }

        @Test
        void handlesWarehouseExceptionTest() {
            User user = new User();
            OrderItemRequest itemRequest = new OrderItemRequest("BEER-1", 2);
            CreateOrderRequest request = new CreateOrderRequest(List.of(itemRequest));

            Beer beer = new Beer();
            beer.setSku("BEER-1");
            beer.setPrice(new BigDecimal("10.00"));

            when(securityContextService.getCurrentUser()).thenReturn(user);
            when(beerRepository.findAllBySku(List.of("BEER-1"))).thenReturn(List.of(beer));

            when(clientService.updateWarehouseInfoByOrder(anyList()))
                    .thenThrow(new WarehouseUpdateServerException("Server unavailable"));

            orderService.createOrder(request);

            verify(orderRepository, times(2)).save(orderCaptor.capture());
            assertEquals(OrderStatus.CANCELLED, orderCaptor.getValue().getStatus());

            verify(emailService, never()).confirmOrderEmail(any(), any(), any());
            verify(emailService, never()).insufficientInventoryOrderEmail(any(), any());
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
