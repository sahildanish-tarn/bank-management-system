package com.jsp.bank_management_system.exception;

public class InvalidEmailException extends RuntimeException
{
    public InvalidEmailException(String message) {
        super(message);
    }
}
