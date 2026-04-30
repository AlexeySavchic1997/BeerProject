package by.alexeysavchic.beer_pet_project.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ExpiredJwtTokenException.class, InvalidTokenException.class, MalformedJwtTokenException.class,
            SecurityJwtException.class, UnsupportedJwtTokenException.class, WrongPasswordException.class,
            RefreshTokenIsAbsentException.class, WrongTokenTypeException.class, UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class, UnknownBeerBrandException.class})
    public ResponseEntity<String> authentificationExceptions(
            RuntimeException ex) {
        logger.error(ex.getMessage(), ex.getCause());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class, BeerNotFoundException.class,
            BeerBrandNotFoundException.class})
    public ResponseEntity<String> notFoundExceptions(
            RuntimeException ex) {
        logger.error(ex.getMessage(), ex.getCause());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
