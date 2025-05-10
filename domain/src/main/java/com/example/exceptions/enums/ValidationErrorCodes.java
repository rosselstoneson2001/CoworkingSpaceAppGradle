package com.example.exceptions.enums;

/**
 * Enum representing error codes for various validation-related errors.
 * Each enum constant corresponds to a specific type of error encountered
 * during validation operations, such as invalid inputs or missing fields.
 * The error codes are used to identify and categorize validation errors
 * in the system.
 *
 * <p>The error codes follow a specific format where the first part represents
 * the category of the error (e.g., "VL" for validation errors), followed by a
 * unique number to distinguish between different types of validation errors.</p>
 */
public enum ValidationErrorCodes {

    INVALID_INPUT("VL303"),
    INVALID_CHOICE("VL307"),
    INVALID_DATE("VL300"),
    MISSING_FIELD("VL310");

    private final String code;

    ValidationErrorCodes(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }
}