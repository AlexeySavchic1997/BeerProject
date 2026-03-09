package by.alexeysavchic.exception;

public class MalformedXmlFileException extends RuntimeException {
    public MalformedXmlFileException(String file) {
        super(String.format(ErrorMessages.MalformedXmlFileException, file));
    }
}
