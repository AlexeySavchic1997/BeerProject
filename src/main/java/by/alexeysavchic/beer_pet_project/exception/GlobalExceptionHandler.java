package by.alexeysavchic.beer_pet_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler({ExpiredJwtTokenException.class, InvalidTokenException.class, MalformedJwtTokenException.class,
    SecurityJwtException.class, UnsupportedJwtTokenException.class, WrongPasswordException.class,
            RefreshTokenIsAbsentException.class, WrongTokenType.class})
    public ResponseEntity<String> authentificationExceptions(
            RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> notFoundExceptions(
            RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
