package com.cabinet.springbootcabinetcomptablemanagement.exceptions;

/**
 * Exception thrown when file storage operations fail
 * Examples: Failed to save file, failed to read file, failed to delete file
 *
 * HTTP Status: 500 INTERNAL SERVER ERROR
 */
public class FileStorageException extends RuntimeException {

    /**
     * Constructor with message
     * @param message Error message describing the file storage failure
     */
    public FileStorageException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause The underlying cause of the exception (usually IOException)
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

