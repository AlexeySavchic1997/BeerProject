package by.alexeysavchic.beer_pet_project.exception;

public class CannotFindBeerExcepton extends RuntimeException{
    public CannotFindBeerExcepton(String sku) {
        super(String.format(ErrorMessages.cannotFindBeer, sku));
    }
}
