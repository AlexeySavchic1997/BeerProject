package by.alexeysavchic.exception;

public class NotEnoughItemsException extends RuntimeException{
    public NotEnoughItemsException() {
        super(ErrorMessages.notEnoughItems);
    }
}
