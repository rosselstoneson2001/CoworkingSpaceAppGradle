package com.example.exceptions;

import com.example.exceptions.enums.ErrorCodes;

public class InvalidWorkspaceException extends RuntimeException {

    private final ErrorCodes errorCode;

    public InvalidWorkspaceException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = ErrorCodes.INVALID_WORKSPACE;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
