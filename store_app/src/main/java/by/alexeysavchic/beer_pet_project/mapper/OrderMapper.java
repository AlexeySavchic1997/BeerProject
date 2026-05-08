package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.response.GetOrderItemResponse;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderResponse;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.UpdateBySubscribeRequest;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {
    GetOrderResponse orderToOrderResponse(Order order);

    @Mapping(target = "beerName", expression = "java(orderItem.getBeer().getName())")
    GetOrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "removeCart", ignore = true)
    @Mapping(target = "cartOrBuilderList", ignore = true)
    @Mapping(target = "cartBuilderList", ignore = true)
    @Mapping(target = "userId", expression = "java(order.getUser().getId())")
    @Mapping(target = "cartList", expression = "java(orderItemsToUpdatePacketRequest(order.getOrderItems()))")
    UpdateBySubscribeRequest orderToUpdateBySubscribeRequest(Order order);

    List<UpdateBeerRequest> orderItemsToUpdatePacketRequest(List<OrderItem> orderItems);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "sku", expression = "java(orderItem.getBeer().getSku())")
    @Mapping(target = "amount", expression = "java(orderItem.getQuantity())")
    UpdateBeerRequest orderItemToUpdatePacketRequest(OrderItem orderItem);
}
