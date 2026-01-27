package by.alexeysavchic.beer_pet_project.exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException()
    {
        super(ErrorMessages.userNotFound);
    }
}
