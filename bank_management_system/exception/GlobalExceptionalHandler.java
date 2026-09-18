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
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> accountNotFoundException(AccountNotFoundException ex) {

        return buildError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNumberDoesNotExists.class)
    public ResponseEntity<Map<String, String>> accountNumberDoesNotExists(AccountNumberDoesNotExists ex) {

        return buildError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountAlreadyClosedException.class)
    public ResponseEntity<Map<String, String>> accountAlreadyClosedException(AccountAlreadyClosedException ex) {
        return buildError(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<Map<String, String>> accountBlockedException(AccountBlockedException ex) {

        return buildError(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountClosedException.class)
    public ResponseEntity<Map<String, String>> accountClosedException(AccountClosedException ex) {
        return buildError(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<Map<String, String>> loanNotFoundException(LoanNotFoundException ex) {
        return buildError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Map<String, String>> transactionNotFoundException(TransactionNotFoundException ex) {
        return buildError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> insufficientBalanceException(InsufficientBalanceException ex) {
        return buildError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, String>> invalidAmountException(InvalidAmountException ex) {
        return buildError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<Map<String, String>> sameAccountTransferException(SameAccountTransferException ex) {
        return buildError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgumentException(IllegalArgumentException ex) {
        return buildError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> genericException(Exception ex) {
        log.error("Unhandled exception: ", ex);
        Map<String, String> error = new HashMap<>();
        error.put("message", "Something went wrong. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ResponseEntity<Map<String, String>> buildError(RuntimeException ex, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(status).body(error);
    }
}