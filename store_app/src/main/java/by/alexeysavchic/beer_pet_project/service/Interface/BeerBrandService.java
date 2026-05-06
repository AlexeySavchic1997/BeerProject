package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetBeerBrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeerBrandService {
    public void addBeerBrand(AddBeerBrandRequest request);

    public void deleteBeerBrand(String brandName);

    public Page<GetBeerBrandResponse> getBeerBrands(String name, Pageable pageable);
}
