package com.example.exceptions;

import com.example.exceptions.enums.ErrorCodes;

public class InvalidReservationException extends RuntimeException {

    private final ErrorCodes errorCode;

    public InvalidReservationException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
