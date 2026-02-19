package by.alexeysavchic.beer_pet_project.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String email) {
        super(String.format(ErrorMessages.wrongPassword, email));
    }
}
