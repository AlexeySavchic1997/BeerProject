package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.WarehouseBeerInfoItem;
import warehouse_api.Zone;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GrpcMapper {

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    @Mapping(target = "zoneTypeValue", ignore = true)
    @Mapping(target = "mergeTime", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "time", expression = "java(localDateTimeToTimestamp(request.getLastModifiedDate()))")
    GetWarehouseInfoRequest getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(GetWarehouseBeerInfoRequest request);


    default Timestamp localDateTimeToTimestamp(LocalDateTime time) {
        return Timestamp.newBuilder().setSeconds(time.toInstant(ZoneOffset.UTC).getEpochSecond()).build();
    }


    List<GetWarehouseBeerInfoResponse> listWarehouseBeerInfoToListGetWarehouseBeerInfoResponse(List<WarehouseBeerInfoItem> beerInfoList);

    @Mapping(target = "zoneType", expression = "java(mapZone(dto.getZoneType()))")
    GetWarehouseBeerInfoResponse WarehouseInfoDTOToWarehouseInfoBeerDTOResponse(WarehouseBeerInfoItem dto);

    default Zone mapZone(Zone zone) {
        return zone == warehouse_api.Zone.UNRECOGNIZED ? null : zone;
    }

    List<UpdateBeerRequest> listOrderItemRequestToListUpdateBeerRequest(List<OrderItemRequest> orderRequest);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    UpdateBeerRequest OrderItemRequestToUpdateBeerRequest(OrderItemRequest request);


}
