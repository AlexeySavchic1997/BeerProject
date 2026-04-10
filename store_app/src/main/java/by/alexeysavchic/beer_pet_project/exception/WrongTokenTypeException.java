package by.alexeysavchic.beer_pet_project.exception;

public class WrongTokenTypeException extends RuntimeException {
    public WrongTokenTypeException(String receivedType, String expectedType) {
        super(String.format(ErrorMessages.wrongTokenType, receivedType, expectedType));
    }
}
