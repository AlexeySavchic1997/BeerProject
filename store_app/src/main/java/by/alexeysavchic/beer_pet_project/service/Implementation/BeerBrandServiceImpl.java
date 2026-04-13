package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandRequest;
import by.alexeysavchic.beer_pet_project.dto.response.BeerBrandResponse;
import by.alexeysavchic.beer_pet_project.entity.BeerBrand;
import by.alexeysavchic.beer_pet_project.exception.BeerBrandNotFoundException;
import by.alexeysavchic.beer_pet_project.mapper.BeerMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerBrandRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.specifications.BeerBrandSpecifications;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerBrandService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Getter
@Setter
@RequiredArgsConstructor
@Service
public class BeerBrandServiceImpl implements BeerBrandService {

    private final BeerBrandRepository beerBrandRepository;

    private final BeerMapper beerMapper;

    private final BeerBrandSpecifications specifications;


    @Override
    public void addBeerBrand(@Valid AddBeerBrandRequest request) {
        if (beerBrandRepository.existsByBrandName(request.getBrandName())) {
            beerBrandRepository.save(beerMapper.addBeerBrandInDBRequest(request));
        } else {
            throw new BeerBrandNotFoundException();
        }
    }

    @Override
    public void deleteBeerBrand(String brandName) {
        BeerBrand beerBrand = beerBrandRepository.
                findByBrandName(brandName).orElseThrow(() -> new BeerBrandNotFoundException());
        beerBrandRepository.delete(beerBrand);
    }

    @Override
    public Page<BeerBrandResponse> getBeerBrands(String brandName, Pageable pageable) {

        Specification<BeerBrand> nameSpecification = Specification.where(specifications.getNameSpecification(brandName));
        Page<BeerBrand> brandPage = beerBrandRepository.findAll(nameSpecification, pageable);
        if (brandPage != null) {
            return brandPage.map(beerBrand -> beerMapper.beerBrandToBeerBrandResponse(beerBrand));
        } else {
            return Page.empty();
        }
    }
}
