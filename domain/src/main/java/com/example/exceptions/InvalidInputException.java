package com.example.exceptions;

import com.example.exceptions.enums.ErrorCodes;

public class InvalidInputException extends RuntimeException {

    private final ErrorCodes errorCode;

    public InvalidInputException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
