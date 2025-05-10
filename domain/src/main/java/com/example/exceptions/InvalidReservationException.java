package com.example.exceptions;

import com.example.exceptions.enums.ValidationErrorCodes;


/**
 * Custom exception class to represent invalid reservation errors.
 * This exception is thrown when a reservation is invalid.
 */
public class InvalidReservationException extends RuntimeException {

    private final ValidationErrorCodes errorCode;

    /**
     * Constructs a new InvalidReservationException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link ValidationErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public InvalidReservationException(ValidationErrorCodes errorCode, String message) {
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
