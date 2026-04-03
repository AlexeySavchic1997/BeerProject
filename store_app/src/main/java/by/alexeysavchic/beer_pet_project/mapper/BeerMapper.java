package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.AddNewBeer;
import by.alexeysavchic.beer_pet_project.dto.request.AddNewBeerCharacteristics;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerCharacteristics;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeerMapper
{
    @Mapping(target = "beerBrand", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userSubscriptions", ignore = true)
    Beer AddNewBeerToBeer(AddNewBeer newBeer);

    @Mapping(target = "beer", ignore = true)
    BeerCharacteristics addNewBeerCharacteristicsToBeerCharacteristics (AddNewBeerCharacteristics characteristics);

    @AfterMapping
    protected void addPollToOptions(@MappingTarget Beer beer)
    {
        if(beer.getCharacteristics()!=null)
        {
            beer.getCharacteristics().forEach(characteristic->characteristic.setBeer(beer));
        }
    }
}
