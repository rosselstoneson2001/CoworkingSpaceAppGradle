package com.example.domain.exceptions;

import com.example.domain.exceptions.enums.RepositoryErrorCodes;

/**
 * Custom exception class to represent invalid input errors.
 * This exception is thrown when the input provided by the user is invalid.
 */
public class InvalidInputException extends RuntimeException {

    private final RepositoryErrorCodes errorCode;

    /**
     * Constructs a new InvalidInputException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link RepositoryErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public InvalidInputException(RepositoryErrorCodes errorCode, String message) {
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
