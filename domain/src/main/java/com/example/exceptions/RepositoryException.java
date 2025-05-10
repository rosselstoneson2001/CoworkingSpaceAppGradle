package com.example.exceptions;

import com.example.exceptions.enums.RepositoryErrorCodes;

/**
 * Custom exception class to represent database-related errors.
 * This exception is thrown when a database operation fails or encounters an error.
 */
    public class RepositoryException extends RuntimeException {

    private final RepositoryErrorCodes errorCode;

    /**
     * Constructs a new RepositoryException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link RepositoryErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public RepositoryException(RepositoryErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return The {@link RepositoryErrorCodes} enum value representing the specific error.
     */
    public RepositoryErrorCodes getErrorCode() {
        return errorCode;
    }
}
