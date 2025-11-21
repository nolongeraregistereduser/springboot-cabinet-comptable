package com.cabinet.springbootcabinetcomptablemanagement.exceptions;

/**
 * Exception thrown when an uploaded file is invalid
 * Examples: Wrong file type, file too large, empty file
 *
 * HTTP Status: 400 BAD REQUEST
 */
public class InvalidFileException extends RuntimeException {

    /**
     * Constructor with message
     * @param message Error message describing why the file is invalid
     */
    public InvalidFileException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause The underlying cause of the exception
     */
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}

