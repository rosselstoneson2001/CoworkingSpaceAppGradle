package com.example.domain.exceptions;


import com.example.domain.exceptions.enums.RepositoryErrorCodes;
import com.example.domain.exceptions.enums.NotFoundErrorCodes;

/**
 * Custom exception class to represent the case when a workspace is not found.
 * This exception is thrown when an attempt to find a workspace fails.
 */
public class WorkspaceNotFoundException extends RuntimeException {

    private final NotFoundErrorCodes errorCode;

    /**
     * Constructs a new WorkspaceNotFoundException with the specified error code and detail message.
     *
     * @param errorCode The error code associated with the exception, which can be found in the {@link RepositoryErrorCodes} enum.
     * @param message   The detail message to be passed with the exception.
     */
    public WorkspaceNotFoundException(NotFoundErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = NotFoundErrorCodes.WORKSPACE_NOT_FOUND;
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
