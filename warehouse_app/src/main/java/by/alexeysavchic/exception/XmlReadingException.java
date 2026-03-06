package by.alexeysavchic.exception;

public class XmlReadingException extends RuntimeException {
    public XmlReadingException() {
        super(ErrorMessages.xmlReadingException);
    }
}
