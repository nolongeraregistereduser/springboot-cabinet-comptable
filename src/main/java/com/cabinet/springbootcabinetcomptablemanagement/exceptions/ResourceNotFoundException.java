package com.cabinet.springbootcabinetcomptablemanagement.exceptions;

/**
 * Exception thrown when a requested resource is not found in the database
 * Examples: Document not found, User not found, Société not found
 *
 * HTTP Status: 404 NOT FOUND
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor with message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Factory method for common use case
     */
    public static ResourceNotFoundException of(String resourceName, Long id) {
        return new ResourceNotFoundException(
            String.format("%s avec l'ID %d n'existe pas", resourceName, id)
        );
    }
    
    /**
     * Factory method for non-ID lookups
     */
    public static ResourceNotFoundException ofField(String resourceName, String field, String value) {
        return new ResourceNotFoundException(
            String.format("%s avec %s '%s' n'existe pas", resourceName, field, value)
        );
    }
}


