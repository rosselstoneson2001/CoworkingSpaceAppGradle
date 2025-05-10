package com.example.exceptions;

import com.example.exceptions.enums.ErrorCodes;

public class InvalidUserException extends RuntimeException {

    private final ErrorCodes errorCode;

    public InvalidUserException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
