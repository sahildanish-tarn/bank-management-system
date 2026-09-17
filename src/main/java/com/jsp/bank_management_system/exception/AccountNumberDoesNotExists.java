package com.jsp.bank_management_system.exception;

public class AccountNumberDoesNotExists extends RuntimeException {
    public AccountNumberDoesNotExists(String message) {
        super(message);
    }
}
