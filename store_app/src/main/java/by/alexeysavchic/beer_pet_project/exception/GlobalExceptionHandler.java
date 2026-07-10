package by.alexeysavchic.beer_pet_project.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({ExpiredJwtTokenException.class, InvalidTokenException.class, MalformedJwtTokenException.class,
            SecurityJwtException.class, UnsupportedJwtTokenException.class, WrongPasswordException.class,
            RefreshTokenIsAbsentException.class, WrongTokenTypeException.class, UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class, UnknownBeerBrandException.class, BeerBrandAlreadyExistsException.class})
    public ResponseEntity<String> authentificationExceptions(
            RuntimeException ex) {
        logger.error(ex.getMessage(), ex.getCause());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class, BeerNotFoundException.class,
            BeerBrandNotFoundException.class})
    public ResponseEntity<String> notFoundExceptions(
            RuntimeException ex) {
        logger.error(ex.getMessage(), ex.getCause());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
