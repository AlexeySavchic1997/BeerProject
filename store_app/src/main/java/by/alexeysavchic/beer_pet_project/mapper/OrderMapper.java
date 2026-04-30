package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.response.OrderItemResponse;
import by.alexeysavchic.beer_pet_project.dto.response.OrderResponse;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {
    OrderResponse orderToOrderResponse(Order order);

    @Mapping(target = "beerName", expression = "java(orderItem.getBeer().getName())")
    OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem);
}
