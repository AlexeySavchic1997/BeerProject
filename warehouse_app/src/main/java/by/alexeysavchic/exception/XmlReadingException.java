package by.alexeysavchic.exception;

public class XmlReadingException extends RuntimeException {
    public XmlReadingException(String file) {
        super(String.format(ErrorMessages.xmlReadingException, file));
    }
}
