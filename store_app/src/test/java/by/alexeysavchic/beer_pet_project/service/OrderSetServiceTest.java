package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.GetOrderSetsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderSetSplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.Gender;
import by.alexeysavchic.beer_pet_project.entity.enums.Location;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.SplitType;
import by.alexeysavchic.beer_pet_project.repository.OrderSetRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.OrderSetServiceImpl;
import by.alexeysavchic.beer_pet_project.service.Implementation.specifications.OrderSetSpecifications;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderSetServiceTest {

    @Mock
    private OrderSetRepository orderSetRepository;

    @Mock
    private OrderSetSpecifications specifications;

    @InjectMocks
    private OrderSetServiceImpl orderSetService;

    @Captor
    private ArgumentCaptor<List<OrderSet>> orderSetListCaptor;

    @Nested
    class getOrderSetsTests {

        @Test
        @SuppressWarnings("unchecked")
        void successfulGetOrderSetsTest() {
            GetOrderSetsRequest request = new GetOrderSetsRequest(OrderSetStatus.WAITING_FOR_SPLIT, SplitType.GENDER, OrderType.FAVORITE_BEER);

            Order order1 = new Order();
            order1.setOrderGender(Gender.MALE);
            order1.setOrderLocation(Location.BLR);

            Order order2 = new Order();
            order2.setOrderGender(Gender.MALE);
            order2.setOrderLocation(Location.BLR);

            OrderSet orderSet = new OrderSet();
            orderSet.setId(10L);
            orderSet.setOrderSetStatus(OrderSetStatus.WAITING_FOR_SPLIT);

            orderSet.setOrders(new ArrayList<>(Arrays.asList(order1, order2)));

            Specification<OrderSet> mockSpec = mock(Specification.class);
            when(specifications.getOrderTypeSpecification(any())).thenReturn(mockSpec);
            when(specifications.getTagSpecification(any())).thenReturn(mockSpec);
            when(specifications.getStatusSpecification(any())).thenReturn(mockSpec);

            when(orderSetRepository.findAll(any(Specification.class))).thenReturn(List.of(orderSet));

            List<GetOrderSetResponse> responses = orderSetService.getOrderSets(request);

            assertNotNull(responses);
            assertEquals(1, responses.size());

            GetOrderSetResponse response = responses.get(0);
            assertEquals(10L, response.getId());
            assertEquals(OrderSetStatus.WAITING_FOR_SPLIT, response.getStatus());
            assertEquals(2, response.getCommonQuantity());

            assertEquals(2, response.getGenderSplit().get(Gender.MALE));
            assertEquals(2, response.getLocationSplit().get(Location.BLR));

            verify(orderSetRepository, times(1)).findAll(any(Specification.class));
        }
    }

    @Nested
    class markSplitTests {

        @Test
        void successfulMarkSplitTest() {
            OrderSetSplitRequest request = new OrderSetSplitRequest(List.of(1L, 2L), SplitType.LOCATION);

            OrderSet set1 = new OrderSet();
            set1.setId(1L);
            OrderSet set2 = new OrderSet();
            set2.setId(2L);

            when(orderSetRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(set1, set2));

            orderSetService.markSplit(request);

            verify(orderSetRepository, times(1)).saveAll(orderSetListCaptor.capture());
            List<OrderSet> savedSets = orderSetListCaptor.getValue();

            assertEquals(2, savedSets.size());
            for (OrderSet savedSet : savedSets) {
                assertEquals(OrderSetStatus.WAITING_FOR_SPLIT, savedSet.getOrderSetStatus());
                assertEquals(SplitType.LOCATION, savedSet.getSplitType());
            }
        }
    }

    @Nested
    class splitTests {

        @Test
        void successfullySplitsByGender() {
            Order orderMale = new Order();
            orderMale.setOrderGender(Gender.MALE);
            orderMale.setOrderType(OrderType.FAVORITE_BEER);

            Order orderFemale = new Order();
            orderFemale.setOrderGender(Gender.FEMALE);
            orderFemale.setOrderType(OrderType.FAVORITE_BEER);

            OrderSet originalSet = new OrderSet();
            originalSet.setSplitType(SplitType.GENDER);
            originalSet.setOrders(new ArrayList<>(Arrays.asList(orderMale, orderFemale)));

            when(orderSetRepository.findTopByOrderSetStatus(OrderSetStatus.WAITING_FOR_SPLIT)).thenReturn(originalSet);

            orderSetService.split();

            verify(orderSetRepository, times(1)).saveAll(orderSetListCaptor.capture());
            List<OrderSet> savedSets = orderSetListCaptor.getValue();

            assertEquals(3, savedSets.size());

            assertTrue(originalSet.getOrders().isEmpty());
            assertEquals(OrderSetStatus.DONE, originalSet.getOrderSetStatus());

            long maleSets = savedSets.stream()
                    .filter(set -> !set.getOrders().isEmpty() && set.getOrders().get(0).getOrderGender() == Gender.MALE)
                    .count();
            long femaleSets = savedSets.stream()
                    .filter(set -> !set.getOrders().isEmpty() && set.getOrders().get(0).getOrderGender() == Gender.FEMALE)
                    .count();

            assertEquals(1, maleSets);
            assertEquals(1, femaleSets);
        }

        @Test
        void successfullySplitsByLocation() {
            Order orderBLR1 = new Order();
            orderBLR1.setOrderLocation(Location.BLR);
            Order orderBLR2 = new Order();
            orderBLR2.setOrderLocation(Location.BLR);

            Order orderRUS = new Order();
            orderRUS.setOrderLocation(Location.RUS);

            OrderSet originalSet = new OrderSet();
            originalSet.setSplitType(SplitType.LOCATION);
            originalSet.setOrders(new ArrayList<>(Arrays.asList(orderBLR1, orderBLR2, orderRUS)));

            when(orderSetRepository.findTopByOrderSetStatus(OrderSetStatus.WAITING_FOR_SPLIT)).thenReturn(originalSet);

            orderSetService.split();

            verify(orderSetRepository, times(1)).saveAll(orderSetListCaptor.capture());
            List<OrderSet> savedSets = orderSetListCaptor.getValue();

            assertEquals(3, savedSets.size());

            OrderSet BLRSet = savedSets.stream()
                    .filter(set -> !set.getOrders().isEmpty() && set.getOrders().get(0).getOrderLocation() == Location.BLR)
                    .findFirst().orElseThrow();

            assertEquals(2, BLRSet.getOrders().size());
            assertEquals(OrderSetStatus.READY_TO_SPLIT, BLRSet.getOrderSetStatus());
        }

        @Test
        void assignsSplitErrorStatusIfOriginalSetIsNotEmpty() {
            Order badOrder = new Order();

            OrderSet originalSet = new OrderSet();
            originalSet.setSplitType(SplitType.LOCATION);
            originalSet.setOrders(new ArrayList<>(List.of(badOrder)));

            when(orderSetRepository.findTopByOrderSetStatus(OrderSetStatus.WAITING_FOR_SPLIT)).thenReturn(originalSet);

            orderSetService.split();

            assertEquals(OrderSetStatus.SPLIT_ERROR, originalSet.getOrderSetStatus());
        }
    }
}