package com.jsp.bank_management_system.exception;

public class AccountAlreadyClosedException extends RuntimeException {
    public AccountAlreadyClosedException(String accountIsAlreadyClosed) {
        super(accountIsAlreadyClosed);
    }
}
