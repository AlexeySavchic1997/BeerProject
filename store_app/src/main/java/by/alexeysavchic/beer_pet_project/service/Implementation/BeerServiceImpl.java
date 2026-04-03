package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.AddNewBeer;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.exception.CannotFindBeerExcepton;
import by.alexeysavchic.beer_pet_project.exception.UnknownBeerBrandException;
import by.alexeysavchic.beer_pet_project.mapper.BeerMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerBrandRepository;
import by.alexeysavchic.beer_pet_project.repository.BeerCharacteristicsRepository;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class BeerServiceImpl implements BeerService
{
    private final BeerMapper mapper;

    private final BeerRepository beerRepository;

    private final BeerCharacteristicsRepository beerCharacteristicsRepository;

    private final BeerBrandRepository beerBrandRepository;


    @Override
    public void addNewBeer(AddNewBeer newBeer)
    {
        Beer beer = mapper.AddNewBeerToBeer(newBeer);
        BeerBrand beerBrand= beerBrandRepository.findByBrandName(newBeer.getName()).
                orElseThrow(()->new UnknownBeerBrandException(newBeer.getBeerBrand()));
        beer.setBeerBrand(beerBrand);
        beerRepository.save(beer);
    }

    @Override
    public void deleteBeer(String sku)
    {
        beerRepository.delete(beerRepository.findBeerBySku(sku).orElseThrow(()->new CannotFindBeerExcepton(sku)));
    }
}
