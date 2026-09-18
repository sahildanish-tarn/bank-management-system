package com.jsp.bank_management_system.exception;

public class UserWithUsernameAlreadyExists extends RuntimeException {
    public UserWithUsernameAlreadyExists(String message) {
        super(message);
    }
}
