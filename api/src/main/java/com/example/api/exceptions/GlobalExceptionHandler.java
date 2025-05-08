package com.example.api.exceptions;

import com.example.domain.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class catches exceptions thrown by controllers and provides structured error responses.
 * It helps in handling different types of exceptions in a central location, improving the readability and maintainability
 * of error handling logic across the application.
 *
 * The exceptions handled include:
 * - Validation errors
 * - Not found errors
 * - Repository errors
 * - General exceptions
 *
 * For each exception type, a structured response with error details is returned, and the error is logged using SLF4J.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger("INTERNAL_LOGGER");

    // -------------------- Validation Exceptions --------------------

    /**
     * Handles invalid input errors.
     *
     * @param ex The InvalidInputException instance.
     * @return A ResponseEntity with error details and HTTP status BAD_REQUEST (400).
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Validation Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles invalid user errors.
     *
     * @param ex The InvalidUserException instance.
     * @return A ResponseEntity with error details and HTTP status BAD_REQUEST (400).
     */
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Validation Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles invalid reservation errors.
     *
     * @param ex The InvalidReservationException instance.
     * @return A ResponseEntity with error details and HTTP status BAD_REQUEST (400).
     */
    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<Object> handleInvalidReservationException(InvalidReservationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Validation Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles invalid workspace errors.
     *
     * @param ex The InvalidWorkspaceException instance.
     * @return A ResponseEntity with error details and HTTP status BAD_REQUEST (400).
     */
    @ExceptionHandler(InvalidWorkspaceException.class)
    public ResponseEntity<Object> handleInvalidWorkspaceException(InvalidWorkspaceException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Validation Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // -------------------- Not Found Exceptions --------------------

    /**
     * Handles user not found errors.
     *
     * @param ex The UserNotFoundException instance.
     * @return A ResponseEntity with error details and HTTP status NOT_FOUND (404).
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundUserException(UserNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Not Found: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles reservation not found errors.
     *
     * @param ex The ReservationNotFoundException instance.
     * @return A ResponseEntity with error details and HTTP status NOT_FOUND (404).
     */
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundReservationException(ReservationNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Not Found: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles workspace not found errors.
     *
     * @param ex The WorkspaceNotFoundException instance.
     * @return A ResponseEntity with error details and HTTP status NOT_FOUND (404).
     */
    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundWorkspaceException(WorkspaceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Not Found: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // -------------------- Repository Exception --------------------

    /**
     * Handles repository-related errors.
     *
     * @param ex The RepositoryException instance.
     * @return A ResponseEntity with error details and HTTP status INTERNAL_SERVER_ERROR (500).
     */
    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<Object> handleRepositoryException(RepositoryException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Status", ex.getErrorCode().getCode());
        errorResponse.put("Error", ex.getErrorCode());
        errorResponse.put("Message", ex.getMessage());
        errorResponse.put("Time", LocalDateTime.now());
        INTERNAL_LOGGER.error("Repository Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // -------------------- General Exceptions --------------------

    /**
     * Handles validation errors, such as invalid request parameters.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity with error details and HTTP status BAD_REQUEST (400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("Time", LocalDateTime.now());
        response.put("Error", "Validation Error");
        response.put("Message", "Invalid request parameters");
        response.put("Status", HttpStatus.BAD_REQUEST.value());
        INTERNAL_LOGGER.error("Validation Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general exceptions.
     *
     * @param ex The Exception instance.
     * @param request The WebRequest instance.
     * @return A ResponseEntity with error details and HTTP status INTERNAL_SERVER_ERROR (500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("Time", LocalDateTime.now());
        response.put("Error", "Internal Server Error");
        response.put("Message", ex.getMessage());
        response.put("Status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        INTERNAL_LOGGER.error("General Error: \nMessage: {} \nDetails: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}