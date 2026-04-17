package by.alexeysavchic.beer_pet_project.mapper;


import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface WarehouseMapper {

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    @Mapping(target = "beer", ignore = true)
    WarehouseBeerInfo getWarehouseBeerInfoResponseToWarehouseBeerInfo(GetWarehouseBeerInfoResponse response);

    @Mapping(target = "adding", ignore = true)
    @Mapping(target = "timeMark", ignore = true)
    UpdateWarehouseInfoDTO warehouseBeerInfoToUpdateWarehouseInfoDTO(WarehouseBeerInfo request);

}
