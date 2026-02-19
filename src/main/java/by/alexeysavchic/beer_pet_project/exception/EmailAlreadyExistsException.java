package by.alexeysavchic.beer_pet_project.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super(String.format(ErrorMessages.emailAlreadyExists, email));
    }
}
