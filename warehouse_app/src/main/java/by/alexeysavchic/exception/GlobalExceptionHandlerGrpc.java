package by.alexeysavchic.exception;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@GrpcAdvice
public class GlobalExceptionHandlerGrpc {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandlerGrpc.class);

    @GrpcExceptionHandler({XmlReadingException.class})
    public Status readingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).withCause(ex);
    }

    @GrpcExceptionHandler({XmlWritingException.class})
    public Status writingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).withCause(ex);
    }

    @GrpcExceptionHandler({NotValidXmlItemException.class})
    public Status validationExceptions(RuntimeException ex) {
        logger.error(ex.getMessage());
        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).withCause(ex);
    }
}
