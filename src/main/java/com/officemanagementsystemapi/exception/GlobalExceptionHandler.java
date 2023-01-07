package com.officemanagementsystemapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GlobalException> handleGlobalException(Exception e) {
        return new ResponseEntity<>(new GlobalException(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<GlobalException> handleUnauthorizedException(BadCredentialsException e) {
        return new ResponseEntity<>(new GlobalException(e), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalException> handleValidationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new GlobalException(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
