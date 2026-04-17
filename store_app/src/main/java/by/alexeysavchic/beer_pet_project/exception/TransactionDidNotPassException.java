package by.alexeysavchic.beer_pet_project.exception;

public class TransactionDidNotPassException extends RuntimeException{
    public TransactionDidNotPassException() {
        super(ErrorMessages.transactionDidNotPass);
    }
}
