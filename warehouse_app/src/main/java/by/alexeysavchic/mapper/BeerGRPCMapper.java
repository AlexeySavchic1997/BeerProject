package by.alexeysavchic.mapper;

import by.alexeysavchic.dto.XmlDto;
import by.alexeysavchic.dto.XmlDtoWrapper;
import by.alexeysavchic.dto.UpdateWarehouseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import warehouse_api.BeerInfo;
import warehouse_api.BeerInfoList;
import warehouse_api.BeerUpdate;
import warehouse_api.BeerUpdateList;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BeerGRPCMapper
{
    public abstract BeerInfoList XmlDtoToGrpcDto(XmlDtoWrapper xmlDtoWrapper);

    public abstract BeerInfo toProto(XmlDto dto);

    public abstract List<UpdateWarehouseDTO> GrpcRequestToXml(BeerUpdateList beerUpdateList);

    public abstract UpdateWarehouseDTO toXML(BeerUpdate dto);
}
