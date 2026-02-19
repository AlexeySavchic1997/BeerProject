package by.alexeysavchic.beer_pet_project.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(ErrorMessages.invalidToken);
    }
}
