package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.AddNewBeer;

public interface BeerService
{
    public void addNewBeer(AddNewBeer beer);

    public void deleteBeer(String sku);
}
