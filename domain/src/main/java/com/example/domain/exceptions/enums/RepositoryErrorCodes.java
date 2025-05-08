package com.example.domain.exceptions.enums;

/**
 * Enum representing error codes for various repository-related errors.
 * Each enum constant corresponds to a specific type of error encountered
 * during database operations. The error codes are used to identify and
 * categorize errors related to database interactions.
 *
 * <p>The error codes follow a specific format where the first part represents
 * the category of the error (e.g., "DB" for database errors), followed by a
 * unique number to distinguish between different types of errors.</p>
 */
public enum RepositoryErrorCodes {

    DATA_INTEGRITY_VIOLATION("DB203"),
    DATA_RETRIEVAL_ERROR("DB209"),
    DATA_REMOVAL_ERROR("DB210"),
    TRANSACTION_FAILED("DB211"),
    DATA_PERSISTENCE_ERROR("DB212"),
    LOAD_FAILED("DB213");

    private final String code;

    RepositoryErrorCodes(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }

}
