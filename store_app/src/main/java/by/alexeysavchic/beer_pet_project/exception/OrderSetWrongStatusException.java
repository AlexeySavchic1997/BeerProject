package by.alexeysavchic.beer_pet_project.exception;

public class OrderSetWrongStatusException extends RuntimeException {
    public OrderSetWrongStatusException() {
        super(ErrorMessages.orderSetWrongType);
    }
}
