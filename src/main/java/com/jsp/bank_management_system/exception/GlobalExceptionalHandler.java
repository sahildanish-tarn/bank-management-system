package com.jsp.bank_management_system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionalHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrorList = ex.getFieldErrors();

        for (FieldError fieldError : fieldErrorList) {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();

            errors.put(field, errorMessage);
        }

        log.error("Validation error: {}", errors);

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> customerNotFoundException(
            CustomerNotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        log.error("CustomerNotFoundException: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Map<String, String>> invalidEmailException(
            InvalidEmailException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        log.error("InvalidEmailException: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> invalidPasswordException(
            InvalidPasswordException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        log.error("InvalidPasswordException: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(UserNotfound.class)
    public ResponseEntity<Map<String, String>> userNotFoundException(
            UserNotfound ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        log.error("UserNotfound: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(UserWithUsernameAlreadyExists.class)
    public ResponseEntity<Map<String, String>> userWithUsernameAlreadyExists(
            UserWithUsernameAlreadyExists ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        log.error("UserWithUsernameAlreadyExists: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}