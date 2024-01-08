package org.example.bookstore.error;


import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.exceptions.AuthenticationException;
import org.example.bookstore.exceptions.EntityAlreadyExistsException;
import org.example.bookstore.exceptions.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDTO> entityNotFound(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return responseEntity("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorDTO> unauthorized(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return responseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ResponseEntity<ErrorDTO> entityAlreadyExists(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return responseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorDTO> responseEntity(String message, HttpStatus status) {
        return responseEntity(message, new HttpHeaders(), status);
    }

    private ResponseEntity<ErrorDTO> responseEntity(String message, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(new ErrorDTO(message), headers, status);
    }
}
