package com.example.exceptions;


import com.example.exceptions.enums.RepositoryErrorCodes;
import com.example.exceptions.enums.NotFoundErrorCodes;

/**
 * Custom exception class to represent reservation not found errors.
 * This exception is thrown when a reservation cannot be found.
 */
public class ReservationNotFoundException extends RuntimeException {

    private final NotFoundErrorCodes errorCode;

    /**
     * Constructs a new ReservationNotFoundException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link RepositoryErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public ReservationNotFoundException(NotFoundErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = NotFoundErrorCodes.RESERVATION_NOT_FOUND;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return The {@link RepositoryErrorCodes} enum value representing the specific error.
     */
    public NotFoundErrorCodes getErrorCode() {
        return errorCode;
    }


}
