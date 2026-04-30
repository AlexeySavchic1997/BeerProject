package by.alexeysavchic.mapper;

import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.UpdateResponseDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import by.alexeysavchic.dto.WarehouseXmlInfoDTO;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.UnpassedOrderResponse;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.WarehouseBeerInfoItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface BeerGRPCMapper {

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    @Mapping(target = "lastModifiedDate", expression = "java(timestampToLocalDateTime(request.getTime()))")
    InputConditionDTO beerRequestToInputCondition(GetWarehouseInfoRequest request);

    default LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds()), ZoneOffset.UTC);
    }

    default BeerInfoResponse XmlDtoToGrpcDto(List<WarehouseXmlInfoDTO> listDTOs) {
        return BeerInfoResponse.newBuilder().addAllBeer(listDtoToListWarehouseBeerInfoItem(listDTOs)).build();
    }

    List<WarehouseBeerInfoItem> listDtoToListWarehouseBeerInfoItem(List<WarehouseXmlInfoDTO> listDTOs);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    @Mapping(target = "zoneTypeValue", ignore = true)
    @Mapping(target = "mergeTime", ignore = true)
    @Mapping(target = "time", expression = "java(localDateTimeToTimestamp(dto.getLastModifiedDate()))")
    WarehouseBeerInfoItem WarehouseInfoDTOToWarehouseInfoBeerDTOResponse(WarehouseXmlInfoDTO dto);

    default Timestamp localDateTimeToTimestamp(LocalDateTime time) {
        return Timestamp.newBuilder().setSeconds(time.toInstant(ZoneOffset.UTC).getEpochSecond()).build();
    }

    List<UpdateWarehouseDTO> updatePacketToListUpdateWarehouseDTO(List<UpdateBeerRequest> updatePacket);

    UpdateWarehouseDTO GrpcRequestToXml(UpdateBeerRequest updateBeerRequest);

    List<UnpassedOrderResponse> listUpdateResponseDTOToListUnpassedOrderResponse(List<UpdateResponseDTO> listUpdateResponseDTO);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    UnpassedOrderResponse UpdateResponseDTOToUnpassedOrderResponse(UpdateResponseDTO updateResponseDTO);
}
