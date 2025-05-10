package com.example.exceptions;

import com.example.exceptions.enums.ValidationErrorCodes;

/**
 * Custom exception class to represent invalid workspace errors.
 * This exception is thrown when a workspace is invalid.
 */
public class InvalidWorkspaceException extends RuntimeException {

    private final ValidationErrorCodes errorCode;

    /**
     * Constructs a new InvalidWorkspaceException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link ValidationErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public InvalidWorkspaceException(ValidationErrorCodes errorCode, String message) {
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
