package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerInDBRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.response.BeerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeerService {
    public Page<BeerResponse> findAll(GetBeerRequest request, Pageable pageable);

    public void addNewBeer(AddBeerInDBRequest beer);

    public void deleteBeer(String sku);
}
