package com.example.exceptions;


import com.example.exceptions.enums.ErrorCodes;

public class WorkspaceNotFoundException extends RuntimeException {

    private final ErrorCodes errorCode;

    public WorkspaceNotFoundException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = ErrorCodes.WORKSPACE_NOT_FOUND;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }
}
