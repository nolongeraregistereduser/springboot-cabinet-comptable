package com.cabinet.springbootcabinetcomptablemanagement.exceptions;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.ApiErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * Catches all exceptions thrown in controllers and formats them into proper API responses
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * This annotation makes this class intercept exceptions from all controllers
 * and automatically convert return values to JSON
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========== CUSTOM EXCEPTIONS ==========

    /**
     * Handle ResourceNotFoundException
     * Returns HTTP 404 NOT FOUND
     *
     * Example triggers:
     * - Document not found by ID
     * - User not found by email
     * - Société not found by ID
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle UnauthorizedException
     * Returns HTTP 403 FORBIDDEN
     *
     * Example triggers:
     * - Société user trying to access another société's documents
     * - Non-comptable trying to validate documents
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUnauthorized(
            UnauthorizedException ex,
            WebRequest request) {

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getMessage())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Handle DuplicateResourceException
     * Returns HTTP 409 CONFLICT
     *
     * Example triggers:
     * - Email already exists when creating user
     * - ICE already exists when creating société
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleDuplicateResource(
            DuplicateResourceException ex,
            WebRequest request) {

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handle InvalidFileException
     * Returns HTTP 400 BAD REQUEST
     *
     * Example triggers:
     * - File type not allowed (not PDF/JPG/PNG)
     * - File size exceeds 10MB
     * - Empty file uploaded
     */
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleInvalidFile(
            InvalidFileException ex,
            WebRequest request) {

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle FileStorageException
     * Returns HTTP 500 INTERNAL SERVER ERROR
     *
     * Example triggers:
     * - Failed to save file to disk
     * - Failed to read file from disk
     * - Failed to delete file
     */
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleFileStorage(
            FileStorageException ex,
            WebRequest request) {

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ========== SPRING SECURITY EXCEPTIONS ==========

    /**
     * Handle BadCredentialsException
     * Returns HTTP 401 UNAUTHORIZED
     *
     * Example triggers:
     * - Wrong password during login
     * - User not found during login
     *
     * Note: We don't reveal which one is wrong for security reasons
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleBadCredentials(
            BadCredentialsException ex,
            WebRequest request) {

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Email ou mot de passe incorrect")
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // ========== VALIDATION EXCEPTIONS ==========

    /**
     * Handle MethodArgumentNotValidException
     * Returns HTTP 400 BAD REQUEST
     *
     * This is triggered when @Valid fails on DTOs
     *
     * Example triggers:
     * - Email format invalid
     * - Password too short
     * - ICE not 15 digits
     * - Required field missing
     *
     * Returns all validation errors in a map format
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        // Extract all field validation errors
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Erreurs de validation");
        response.put("errors", fieldErrors);
        response.put("path", extractPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ========== GENERIC EXCEPTION ==========

    /**
     * Handle all other exceptions
     * Returns HTTP 500 INTERNAL SERVER ERROR
     *
     * This is the catch-all for any unexpected errors
     * Always log these for debugging
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleGlobalException(
            Exception ex,
            WebRequest request) {

        // Log the exception for debugging (in production, use proper logging)
        System.err.println(" Unexpected error: " + ex.getClass().getSimpleName());
        System.err.println("   Message: " + ex.getMessage());
        ex.printStackTrace();

        ApiErrorResponseDTO error = ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Une erreur inattendue s'est produite. Veuillez contacter l'administrateur.")
                .path(extractPath(request))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ========== HELPER METHODS ==========

    /**
     * Extract clean request path from WebRequest
     * @param request The web request
     * @return Clean path without "uri=" prefix
     */
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}

