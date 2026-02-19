package by.alexeysavchic.beer_pet_project.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format(ErrorMessages.userNotFound, id));
    }
}
