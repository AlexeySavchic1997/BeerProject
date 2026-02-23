package by.alexeysavchic.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({XmlReadingException.class, InvalidXmlFileException.class, XmlWritingException.class})
    public void readingExceptions(
            RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
    }

}
