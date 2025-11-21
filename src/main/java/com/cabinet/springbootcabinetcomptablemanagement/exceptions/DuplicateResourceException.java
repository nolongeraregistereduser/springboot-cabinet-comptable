package com.cabinet.springbootcabinetcomptablemanagement.exceptions;

/**
 * Exception thrown when attempting to create a resource that already exists
 * Examples: Email already registered, ICE already exists
 *
 * HTTP Status: 409 CONFLICT
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructor with message
     * @param message Error message describing the duplicate resource
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause The underlying cause of the exception
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Factory method for duplicate email
     * @param email The email that already exists
     * @return DuplicateResourceException with formatted message
     */
    public static DuplicateResourceException forEmail(String email) {
        return new DuplicateResourceException(
            String.format("Un utilisateur avec l'email '%s' existe déjà", email)
        );
    }

    /**
     * Factory method for duplicate ICE
     * @param ice The ICE that already exists
     * @return DuplicateResourceException with formatted message
     */
    public static DuplicateResourceException forIce(String ice) {
        return new DuplicateResourceException(
            String.format("Une société avec l'ICE '%s' existe déjà", ice)
        );
    }
}

