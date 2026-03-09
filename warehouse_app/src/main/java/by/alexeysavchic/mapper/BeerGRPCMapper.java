package by.alexeysavchic.mapper;

import by.alexeysavchic.dto.GetDataFromXmlDTO;
import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import warehouse_api.BeerInfo;
import warehouse_api.BeerInfoResponse;
import warehouse_api.BeerRequest;
import warehouse_api.UpdateBeer;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BeerGRPCMapper {
    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    InputConditionDTO beerRequestToInputCondition(BeerRequest request);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "removeBeer", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "beerOrBuilderList", ignore = true)
    @Mapping(target = "beerBuilderList", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    default BeerInfoResponse XmlDtoToGrpcDto(List<GetDataFromXmlDTO> listDTOs) {
        return BeerInfoResponse.newBuilder().addAllBeer(listDtoToListBeerInfo(listDTOs)).build();
    }

    List<BeerInfo> listDtoToListBeerInfo(List<GetDataFromXmlDTO> listDTOs);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "zoneTypeValue", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    BeerInfo toProto(GetDataFromXmlDTO dto);

    List<UpdateWarehouseDTO> GrpcRequestToXml(List<UpdateBeer> updateBeerList);

    UpdateWarehouseDTO toXML(UpdateBeer dto);
}
