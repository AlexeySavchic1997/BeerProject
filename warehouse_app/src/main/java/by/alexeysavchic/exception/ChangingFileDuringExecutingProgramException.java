package by.alexeysavchic.exception;

public class ChangingFileDuringExecutingProgramException extends RuntimeException {
    public ChangingFileDuringExecutingProgramException(String file) {
        super(String.format(ErrorMessages.ChangingXmlDuringExecuting, file));
    }
}
