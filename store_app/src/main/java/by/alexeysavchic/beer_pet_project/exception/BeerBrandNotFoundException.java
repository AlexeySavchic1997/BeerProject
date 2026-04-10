package by.alexeysavchic.beer_pet_project.exception;

public class BeerBrandNotFoundException extends RuntimeException {
    public BeerBrandNotFoundException() {
        super(ErrorMessages.beerBrandNotFound);
    }
}
