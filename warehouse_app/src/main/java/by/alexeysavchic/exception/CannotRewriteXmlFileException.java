package by.alexeysavchic.exception;

public class CannotRewriteXmlFileException extends RuntimeException {
    public CannotRewriteXmlFileException(String file) {
        super(String.format(ErrorMessages.CannotRewriteFile, file));
    }
}
