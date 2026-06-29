package by.alexeysavchic.beer_pet_project.exception;

public class BeerBrandAlreadyExistsException extends RuntimeException {
    public BeerBrandAlreadyExistsException(String brandName) {
        super(String.format(ErrorMessages.beerBrandAlreadyExists, brandName));
    }
}
