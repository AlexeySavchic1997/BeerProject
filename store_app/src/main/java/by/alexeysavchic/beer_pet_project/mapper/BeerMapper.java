package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandRequest;
import by.alexeysavchic.beer_pet_project.dto.request.AddBeerCharacteristicRequest;
import by.alexeysavchic.beer_pet_project.dto.request.AddBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.response.BeerBrandResponse;
import by.alexeysavchic.beer_pet_project.dto.response.BeerResponse;
import by.alexeysavchic.beer_pet_project.dto.response.CharacteristicsResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.entity.BeerCharacteristics;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeerMapper {
    @Mapping(target = "beerBrand", expression = "java(beer.getBeerBrand().getBrandName())")
    @Mapping(target = "amount", expression = "java(listWarehouseInfoToAmount(beer.getWarehouseBeerInfos()))")
    BeerResponse beerToBeerResponse(Beer beer);

    default Integer listWarehouseInfoToAmount(List<WarehouseBeerInfo> warehouseBeerInfoList) {
        return warehouseBeerInfoList.get(0).getAmount();
    }

    CharacteristicsResponse beerCharacteristicsToCharacteristicsResponse(BeerCharacteristics characteristics);

    @Mapping(target = "id", ignore = true)
    BeerBrand addBeerBrandInDBRequest(AddBeerBrandRequest request);

    BeerBrandResponse beerBrandToBeerBrandResponse(BeerBrand beerBrand);

    @Mapping(target = "beerBrand", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSubscriptions", ignore = true)
    @Mapping(target = "warehouseBeerInfos", ignore = true)
    Beer AddNewBeerToBeer(AddBeerRequest newBeer);

    @Mapping(target = "beer", ignore = true)
    BeerCharacteristics addNewBeerCharacteristicsToBeerCharacteristics(AddBeerCharacteristicRequest characteristics);

    @AfterMapping
    default void addBeerToBeerCharacteristics(@MappingTarget Beer beer) {
        if (beer.getCharacteristics() != null) {
            beer.getCharacteristics().forEach(characteristic -> characteristic.setBeer(beer));
        }
    }
}
