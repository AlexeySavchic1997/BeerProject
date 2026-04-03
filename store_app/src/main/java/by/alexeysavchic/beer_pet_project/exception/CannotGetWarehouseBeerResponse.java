package by.alexeysavchic.beer_pet_project.exception;

public class CannotGetWarehouseBeerResponse extends RuntimeException
{
    public CannotGetWarehouseBeerResponse(String message, Throwable cause) {
        super(message ,cause);
    }
}
