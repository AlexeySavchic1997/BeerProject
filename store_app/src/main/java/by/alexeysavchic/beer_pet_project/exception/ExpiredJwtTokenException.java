package by.alexeysavchic.beer_pet_project.exception;

public class ExpiredJwtTokenException extends RuntimeException {
    public ExpiredJwtTokenException() {
        super(ErrorMessages.expiredToken);
    }
}
