package by.alexeysavchic.beer_pet_project.exception;

public class WrongPasswordException extends RuntimeException
{
    public WrongPasswordException() {
        super(ErrorMessages.wrongPassword);
    }
}
