package by.alexeysavchic.beer_pet_project.exception;

public class OrderSetNotFoundException extends RuntimeException {
    public OrderSetNotFoundException() {
        super(ErrorMessages.orderSetNotFound);
    }
}
