package by.alexeysavchic.beer_pet_project.exception;

public class MalformedJwtTokenException extends RuntimeException {
    public MalformedJwtTokenException() {
        super(ErrorMessages.malformedToken);
    }
}
