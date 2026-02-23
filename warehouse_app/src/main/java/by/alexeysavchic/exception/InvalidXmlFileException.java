package by.alexeysavchic.exception;

public class InvalidXmlFileException extends RuntimeException
{
    public InvalidXmlFileException() {
        super(ErrorMessages.invalidXmlException);
    }
}
