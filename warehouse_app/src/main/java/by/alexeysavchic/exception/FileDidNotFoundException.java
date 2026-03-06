package by.alexeysavchic.exception;

public class FileDidNotFoundException extends RuntimeException {
    public FileDidNotFoundException(String file) {
        super(String.format(ErrorMessages.fileNotFoundException, file));
    }
}
