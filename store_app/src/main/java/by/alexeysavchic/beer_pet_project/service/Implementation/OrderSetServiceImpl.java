package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.GetOrderSetsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderSetSplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.Gender;
import by.alexeysavchic.beer_pet_project.entity.enums.Location;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.repository.OrderSetRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.specifications.OrderSetSpecifications;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderSetServiceImpl implements OrderSetService {

    private final OrderSetRepository orderSetRepository;

    private final OrderSetSpecifications specifications;


    @Override
    public List<GetOrderSetResponse> getOrderSets(GetOrderSetsRequest request) {
        Specification<OrderSet> specification = Specification.allOf(specifications.getOrderTypeSpecification(request.getOrderType()),
                specifications.getTagSpecification(request.getSplitType()),
                specifications.getStatusSpecification(request.getOrderSetStatus()));
        List<OrderSet> orderSets = orderSetRepository.findAll(specification);
        List<GetOrderSetResponse> responses = new ArrayList<>();
        for (OrderSet orderSet : orderSets) {
            GetOrderSetResponse response = new GetOrderSetResponse();
            response.setStatus(orderSet.getOrderSetStatus());
            response.setId(orderSet.getId());
            response.setCommonQuantity(orderSet.getOrders().size());
            Map<Gender, Integer> genderMap = response.getGenderSplit();
            Map<Location, Integer> locationMap = response.getLocationSplit();
            for (Order order : orderSet.getOrders()) {
                genderMap.merge(order.getOrderGender(), 1, Integer::sum);
                locationMap.merge(order.getOrderLocation(), 1, Integer::sum);
            }
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void markSplit(@Valid OrderSetSplitRequest request) {
        List<OrderSet> orderSets = orderSetRepository.findAllByIdIn(request.getIds());
        for (OrderSet orderSet : orderSets) {
            orderSet.setSplitType(request.getSplitType());
            orderSet.setOrderSetStatus(OrderSetStatus.WAITING_FOR_SPLIT);
        }
        orderSetRepository.saveAll(orderSets);
    }

    @Override
    public void split() {
        OrderSet orderSet = orderSetRepository.findTopByOrderSetStatus(OrderSetStatus.WAITING_FOR_SPLIT);
        List<OrderSet> splitSets = new ArrayList<>();
        List<Order> orders = orderSet.getOrders();
        Iterator<Order> iterator = orders.iterator();
        switch (orderSet.getSplitType()) {
            case LOCATION -> {
                splitByLocation(orderSet, splitSets, iterator);
            }
            case GENDER -> {
                splitByGender(orderSet, splitSets, iterator);
            }
        }
        orderSetRepository.saveAll(splitSets);
    }

    private void splitByLocation(OrderSet orderSet, List<OrderSet> splitSets, Iterator<Order> iterator) {
        Map<Location, OrderSet> splitSetsMap = new EnumMap<>(Location.class);
        while (iterator.hasNext()) {
            Order order = iterator.next();
            splitSetsMap.compute(order.getOrderLocation(), (key, value) ->
            {
                if (value == null) {
                    OrderSet splitSet = new OrderSet();
                    splitSet.setOrderType(order.getOrderType());
                    splitSet.setOrderSetStatus(OrderSetStatus.READY_TO_SPLIT);
                    List<Order> splitOrders = new ArrayList<>();
                    splitOrders.add(order);
                    splitSet.setOrders(splitOrders);
                    return splitSet;
                } else {
                    value.getOrders().add(order);
                    return value;
                }
            });
            iterator.remove();
        }
        splitSets.addAll(splitSetsMap.values());
        splitCheck(orderSet);
        splitSets.add(orderSet);
    }

    private void splitByGender(OrderSet orderSet, List<OrderSet> splitSets, Iterator<Order> iterator) {
        Map<Gender, OrderSet> splitSetsMap = new EnumMap<>(Gender.class);
        while (iterator.hasNext()) {
            Order order = iterator.next();
            splitSetsMap.compute(order.getOrderGender(), (key, value) ->
            {
                if (value == null) {
                    OrderSet splitSet = new OrderSet();
                    splitSet.setOrderType(order.getOrderType());
                    splitSet.setOrderSetStatus(OrderSetStatus.READY_TO_SPLIT);
                    List<Order> splitOrders = new ArrayList<>();
                    splitOrders.add(order);
                    splitSet.setOrders(splitOrders);
                    return splitSet;
                } else {
                    value.getOrders().add(order);
                    return value;
                }
            });
            iterator.remove();
        }
        splitSets.addAll(splitSetsMap.values());
        splitCheck(orderSet);
        splitSets.add(orderSet);
    }

    private void splitCheck(OrderSet orderSet) {
        if (orderSet.getOrders().isEmpty()) {
            orderSet.setOrderSetStatus(OrderSetStatus.DONE);
        } else {
            orderSet.setOrderSetStatus(OrderSetStatus.SPLIT_ERROR);
        }
    }


}
