package by.alexeysavchic.beer_pet_project.exception;

public class UnsupportedJwtTokenException extends RuntimeException {
    public UnsupportedJwtTokenException() {
        super(ErrorMessages.unsupportedToken);
    }
}
