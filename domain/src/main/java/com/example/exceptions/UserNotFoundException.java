package com.example.exceptions;

import com.example.exceptions.enums.ErrorCodes;

public class UserNotFoundException extends RuntimeException {

    private final ErrorCodes errorCode;

    public UserNotFoundException(ErrorCodes errorCodes, String message) {
        super(message);
        this.errorCode = ErrorCodes.INVALID_WORKSPACE;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
