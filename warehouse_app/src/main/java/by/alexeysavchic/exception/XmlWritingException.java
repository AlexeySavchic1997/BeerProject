package by.alexeysavchic.exception;

public class XmlWritingException extends RuntimeException {
    public XmlWritingException(String file) {
        super(String.format(ErrorMessages.xmlWritingException, file));
    }
}
