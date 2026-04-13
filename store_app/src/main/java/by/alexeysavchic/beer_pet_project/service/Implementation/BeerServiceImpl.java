package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.request.BeerCharacteristicsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.response.BeerResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.exception.BeerNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.UnknownBeerBrandException;
import by.alexeysavchic.beer_pet_project.mapper.BeerMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerBrandRepository;
import by.alexeysavchic.beer_pet_project.repository.BeerCharacteristicsRepository;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.specifications.BeerSpecifications;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerMapper mapper;

    private final BeerRepository beerRepository;

    private final BeerCharacteristicsRepository beerCharacteristicsRepository;

    private final BeerBrandRepository beerBrandRepository;

    private final BeerSpecifications specifications;


    @Override
    public void addNewBeer(AddBeerRequest newBeer) {
        BeerBrand beerBrand = beerBrandRepository.findByBrandName(newBeer.getBeerBrand()).
                orElseThrow(() -> new UnknownBeerBrandException(newBeer.getBeerBrand()));
        Beer beer = mapper.AddNewBeerToBeer(newBeer);
        beer.setBeerBrand(beerBrand);
        beerRepository.save(beer);
    }

    @Override
    public void deleteBeer(String sku) {
        beerRepository.delete(beerRepository.findBeerBySku(sku).orElseThrow(() -> new BeerNotFoundException()));
    }

    @Override
    public Page<BeerResponse> findAll(GetBeerRequest request, Pageable pageable) {
        Specification<Beer> specification = Specification.allOf(specifications.getIdSpecification(request.getId()),
                specifications.getSkuSpecification(request.getSku()),
                specifications.getNameSpecification(request.getName()),
                specifications.getVolumeSpecification(request.getVolume()),
                specifications.getPriceSpecification(request.getLowerPrice(), request.getUpperPrice()),
                specifications.getBrandNameSpecification(request.getBeerBrandName()));
        for (BeerCharacteristicsRequest characteristic : request.getCharacteristics()) {
            specification.and(specifications.getCharacteristicSpecification(characteristic.getCharacteristic(),
                    characteristic.getLowerValue(), characteristic.getUpperValue()));
        }
        Page<Beer> beerPage = beerRepository.findAll(specification, pageable);
        if (beerPage != null) {
            return beerPage.map(beer -> mapper.beerToBeerResponse(beer));
        } else {
            throw new BeerNotFoundException();
        }
    }
}
