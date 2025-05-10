package com.example.exceptions.enums;

public enum ErrorCodes {

    RESERVATION_NOT_FOUND("ERR101"),
    WORKSPACE_NOT_FOUND("ERR102"),
    USER_NOT_FOUND("ERR100"),
    INVALID_INPUT("ERR103"),
    DATABASE_ERROR("ERR104"),
    INVALID_RESERVATION("ERR105"),
    INVALID_WORKSPACE("ERR106"),
    INVALID_USER("ERR108"),
    INVALID_CHOICE("ERR107");

    private final String code;

    ErrorCodes(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }

}
