package by.alexeysavchic.beer_pet_project.exception;

public class WrongTokenType extends RuntimeException {
    public WrongTokenType(String receivedType, String expectedType) {
        super(String.format(ErrorMessages.wrongTokenType, receivedType, expectedType));
    }
}
