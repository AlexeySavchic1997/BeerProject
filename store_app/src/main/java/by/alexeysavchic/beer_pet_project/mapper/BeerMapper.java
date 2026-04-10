package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandInDBRequest;
import by.alexeysavchic.beer_pet_project.dto.request.AddBeerCharacteristicsInDBRequest;
import by.alexeysavchic.beer_pet_project.dto.request.AddBeerInDBRequest;
import by.alexeysavchic.beer_pet_project.dto.response.BeerBrandResponse;
import by.alexeysavchic.beer_pet_project.dto.response.BeerResponse;
import by.alexeysavchic.beer_pet_project.dto.response.CharacteristicsResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.entity.BeerCharacteristics;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeerMapper {
    @Mapping(target = "beerBrand", expression = "java(beer.getBeerBrand().getBrandName())")
    BeerResponse beerToBeerResponse(Beer beer);

    CharacteristicsResponse beerCharacteristicsToCharacteristicsResponse(BeerCharacteristics characteristics);

    @Mapping(target = "id", ignore = true)
    BeerBrand addBeerBrandInDBRequest(AddBeerBrandInDBRequest request);

    BeerBrandResponse beerBrandToBeerBrandResponse(BeerBrand beerBrand);

    @Mapping(target = "beerBrand", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSubscriptions", ignore = true)
    Beer AddNewBeerToBeer(AddBeerInDBRequest newBeer);

    @Mapping(target = "beer", ignore = true)
    BeerCharacteristics addNewBeerCharacteristicsToBeerCharacteristics(AddBeerCharacteristicsInDBRequest characteristics);

    @AfterMapping
    default void addBeerToBeerCharacteristics(@MappingTarget Beer beer) {
        if (beer.getCharacteristics() != null) {
            beer.getCharacteristics().forEach(characteristic -> characteristic.setBeer(beer));
        }
    }
}
