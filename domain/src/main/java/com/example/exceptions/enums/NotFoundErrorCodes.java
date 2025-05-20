package com.example.exceptions.enums;

/**
 * Enum representing error codes for different not found exceptions.
 *
 * <p>The error codes follow a specific format where the first part represents
 * the category of the error (e.g., "NF" for Not Found), followed by a unique number
 * to distinguish between different types of errors.</p>
 */
public enum NotFoundErrorCodes {

    RESERVATION_NOT_FOUND("NF101"),
    WORKSPACE_NOT_FOUND("NF102"),
    USER_NOT_FOUND("NF100");

    private final String code;

    NotFoundErrorCodes(String code) {
        this.code = code;

    }

    public String getCode()  {
        return code;
    }
}
