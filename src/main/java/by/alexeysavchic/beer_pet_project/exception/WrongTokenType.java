package by.alexeysavchic.beer_pet_project.exception;

public class WrongTokenType extends RuntimeException
{
    public WrongTokenType() {
        super(ErrorMessages.wrongTokenType);
    }
}
