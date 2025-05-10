package com.example.exceptions;

import com.example.exceptions.enums.ErrorCodes;

public class DatabaseException extends RuntimeException {

    private final ErrorCodes errorCode;

    public DatabaseException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = ErrorCodes.DATABASE_ERROR;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
