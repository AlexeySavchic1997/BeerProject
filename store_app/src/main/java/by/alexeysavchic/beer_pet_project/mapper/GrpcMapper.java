package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.WarehouseBeerInfo;

import java.time.Instant;
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

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    @Mapping(target = "lastModifiedDate", expression = "java(localDateTimeToTimestamp(dto.getLastModifiedDate()))")
    List<GetWarehouseBeerInfoResponse> listWarehouseBeerInfoToListGetWarehouseBeerInfoResponse(List<WarehouseBeerInfo> beerInfoList);


    GetWarehouseBeerInfoResponse WarehouseInfoDTOToWarehouseInfoBeerDTOResponse(WarehouseBeerInfo dto);


    default LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds()), ZoneOffset.UTC);
    }

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "zoneValue", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    UpdateBeerRequest UpdateWarehouseInfoDTOToUpdateBeerRequest(UpdateWarehouseInfoDTO warehouseInfoDTO);


}
