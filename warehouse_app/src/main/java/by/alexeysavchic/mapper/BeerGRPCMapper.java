package by.alexeysavchic.mapper;

import by.alexeysavchic.dto.GetDataFromXmlDTO;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse_api.BeerInfo;
import warehouse_api.BeerInfoResponse;
import warehouse_api.UpdateBeer;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BeerGRPCMapper {
    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "removeBeer", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "beerOrBuilderList", ignore = true)
    @Mapping(target = "beerBuilderList", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    public abstract BeerInfoResponse XmlDtoToGrpcDto(XmlDtoWrapper xmlDtoWrapper);

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "zoneTypeValue", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "skuBytes", ignore = true)
    public abstract BeerInfo toProto(GetDataFromXmlDTO dto);

    public abstract List<UpdateWarehouseDTO> GrpcRequestToXml(List<UpdateBeer> updateBeerList);

    public abstract UpdateWarehouseDTO toXML(UpdateBeer dto);
}
