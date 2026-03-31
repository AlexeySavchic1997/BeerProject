package by.alexeysavchic.exception;

public class XmlWritingException extends RuntimeException {
    public XmlWritingException(String file, Throwable cause) {
        super(String.format(ErrorMessages.xmlWritingException, file), cause);
    }
}
