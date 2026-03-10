package com.banking.exception;

/**
 * Thrown when a user tries to register with an email or phone
 * number that already exists in the system.
 * Maps to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
