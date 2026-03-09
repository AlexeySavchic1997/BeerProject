package by.alexeysavchic.beer_pet_project.exception;

public class RefreshTokenIsAbsentException extends RuntimeException {
    public RefreshTokenIsAbsentException() {
        super(ErrorMessages.absentRefreshToken);
    }
}
