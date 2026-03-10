package com.banking.exception;

/**
 * Thrown when a user tries to withdraw or transfer more money
 * than their current account balance.
 * Maps to HTTP 400 Bad Request.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
