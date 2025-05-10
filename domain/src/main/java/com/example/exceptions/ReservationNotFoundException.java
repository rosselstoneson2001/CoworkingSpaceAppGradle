package com.example.exceptions;


import com.example.exceptions.enums.ErrorCodes;

public class ReservationNotFoundException extends RuntimeException {

    private final ErrorCodes errorCode;

    public ReservationNotFoundException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = ErrorCodes.RESERVATION_NOT_FOUND;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }


}
