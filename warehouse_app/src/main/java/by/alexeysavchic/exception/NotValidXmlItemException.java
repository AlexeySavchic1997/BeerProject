package by.alexeysavchic.exception;

public class NotValidXmlItemException extends RuntimeException {
    public NotValidXmlItemException(String message) {
        super(String.format(ErrorMessages.notValidXmlItem, message));
        ;
    }
}
