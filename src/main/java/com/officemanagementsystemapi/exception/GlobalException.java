package com.officemanagementsystemapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalException {
    private String message;
    private Integer statusCode;
    private String generalMessage;
    private Map<String, List<String>> validationMessages = new HashMap<>();

    public GlobalException(ResponseStatusException exception) {
        this.message = exception.getMessage();
        this.statusCode = exception.getRawStatusCode();
        this.generalMessage = "Something wrong with the server, please contact suppport@gmail.com";
    }

    public GlobalException(Exception exception) {
        this.message = exception.getMessage();
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.generalMessage = "Something wrong with the server, please contact suppport@gmail.com";
    }

    public GlobalException(BadCredentialsException exception) {
        this.message = exception.getMessage();
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
        this.generalMessage = "Invalid email or password";
    }


    public GlobalException(MethodArgumentNotValidException e) {
        this.message = e.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.generalMessage = "Validation failed, Please fix errors";

        e.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            if (validationMessages.containsKey(fieldError.getField())) {
                validationMessages.get(fieldError.getField()).add(fieldError.getDefaultMessage());
            } else {
                List<String> fieldErrorMessages = new ArrayList<>();
                fieldErrorMessages.add(fieldError.getDefaultMessage());
                validationMessages.put(fieldError.getField(), fieldErrorMessages);
            }
        });
    }

    public Map<String, List<String>> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(Map<String, List<String>> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getGeneralMessage() {
        return generalMessage;
    }

    public void setGeneralMessage(String generalMessage) {
        this.generalMessage = generalMessage;
    }
}
