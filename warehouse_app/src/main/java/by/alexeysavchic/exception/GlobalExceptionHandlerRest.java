package by.alexeysavchic.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerRest {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandlerRest.class);

    @ExceptionHandler({XmlReadingException.class})
    public ResponseEntity<String> readingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler({XmlWritingException.class})
    public ResponseEntity<String> writingExceptions(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
