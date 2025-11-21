package com.cabinet.springbootcabinetcomptablemanagement.exceptions;

/**
 * Exception thrown when a user attempts to access a resource they don't have permission to access
 * Example: Société user trying to view another société's documents
 *
 * HTTP Status: 403 FORBIDDEN
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructor with message
     * @param message Error message describing the unauthorized action
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause The underlying cause of the exception
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

