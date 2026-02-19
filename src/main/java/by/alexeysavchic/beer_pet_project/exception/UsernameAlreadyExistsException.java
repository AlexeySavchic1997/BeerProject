package by.alexeysavchic.beer_pet_project.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(String.format(ErrorMessages.usernameAlreadyExists, username));
    }
}
