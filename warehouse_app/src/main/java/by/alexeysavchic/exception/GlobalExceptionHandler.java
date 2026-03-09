package by.alexeysavchic.exception;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@GrpcAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @GrpcExceptionHandler({XmlReadingException.class, InvalidXmlFileException.class, MalformedXmlFileException.class})
    public Status readingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).withCause(ex);
    }

    @GrpcExceptionHandler({XmlWritingException.class})
    public Status writingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).withCause(ex);
    }

    @GrpcExceptionHandler({CannotRewriteXmlFileException.class, FileDidNotFoundException.class})
    public Status fileAccessExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return Status.DATA_LOSS.withDescription(ex.getMessage()).withCause(ex);
    }

    @GrpcExceptionHandler({ChangingFileDuringExecutingProgramException.class})
    public Status executingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return Status.INTERNAL.withDescription(ex.getMessage()).withCause(ex);
    }
}
