package by.alexeysavchic.beer_pet_project.exception;

public class BeerNotFoundException extends RuntimeException {
    public BeerNotFoundException() {
        super(ErrorMessages.beerNotFound);
    }
}
