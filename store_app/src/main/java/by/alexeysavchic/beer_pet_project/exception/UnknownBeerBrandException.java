package by.alexeysavchic.beer_pet_project.exception;

public class UnknownBeerBrandException extends RuntimeException {
    public UnknownBeerBrandException(String brand) {
        super(String.format(ErrorMessages.unknownBeerBrandException, brand));
    }
}
