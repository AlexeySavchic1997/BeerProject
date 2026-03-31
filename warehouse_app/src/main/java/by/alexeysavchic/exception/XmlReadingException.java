package by.alexeysavchic.exception;

public class XmlReadingException extends RuntimeException {
    public XmlReadingException(String file, Throwable cause) {
        super(String.format(ErrorMessages.xmlReadingException, file), cause);
    }
}
