package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerCharacteristicRequest;
import by.alexeysavchic.beer_pet_project.dto.request.AddBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetBeerResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.entity.BeerCharacteristics;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;


class BeerMapperTest {

    private final BeerMapper mapper = Mappers.getMapper(BeerMapper.class);

    @Test
    void successfulBeerToBeerResponse() {
        BeerBrand brand = new BeerBrand();
        brand.setBrandName("Guinness");

        WarehouseBeerInfo warehouseInfo = new WarehouseBeerInfo();
        warehouseInfo.setAmount(150);

        Beer beer = new Beer();
        beer.setBeerBrand(brand);
        beer.setWarehouseBeerInfos(List.of(warehouseInfo));

        GetBeerResponse response = mapper.beerToBeerResponse(beer);

        assertNotNull(response);
        assertEquals("Guinness", response.getBeerBrand());
        assertEquals(150, response.getAmount());
    }

    @Test
    void shouldSetBeerReferenceInCharacteristics_AfterMapping() {
        AddBeerRequest request = new AddBeerRequest();

        AddBeerCharacteristicRequest charRequest1 = new AddBeerCharacteristicRequest();
        AddBeerCharacteristicRequest charRequest2 = new AddBeerCharacteristicRequest();
        request.setCharacteristics(List.of(charRequest1, charRequest2));

        Beer mappedBeer = mapper.AddNewBeerToBeer(request);

        assertNotNull(mappedBeer);
        assertNotNull(mappedBeer.getCharacteristics());
        assertEquals(2, mappedBeer.getCharacteristics().size());

        for (BeerCharacteristics characteristic : mappedBeer.getCharacteristics()) {
            assertSame(mappedBeer, characteristic.getBeer());
        }
    }
}
