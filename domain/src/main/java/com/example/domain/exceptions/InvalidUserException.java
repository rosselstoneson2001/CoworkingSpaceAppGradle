package com.example.domain.exceptions;

import com.example.domain.exceptions.enums.ValidationErrorCodes;

/**
 * Custom exception class to represent invalid user errors.
 * This exception is thrown when a user is invalid.
 */
public class InvalidUserException extends RuntimeException {

    private final ValidationErrorCodes errorCode;

    /**
     * Constructs a new InvalidUserException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link ValidationErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public InvalidUserException(ValidationErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return The {@link ValidationErrorCodes} enum value representing the specific error.
     */
    public ValidationErrorCodes getErrorCode() {
        return errorCode;
    }
}
