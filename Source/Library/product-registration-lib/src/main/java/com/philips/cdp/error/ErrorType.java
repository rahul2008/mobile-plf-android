package com.philips.cdp.error;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ErrorType {

    UNKNOWN(-1, "unknown"),
    ACCESS_TOKEN_INVALID(403, "malformed access token"),
    ACCESS_TOKEN_EXPIRED(500, "access token expired"),
    REFRESH_ACCESS_TOKEN_FAILED(500, "access token expired"),
    INVALID_VALIDATION(422, "Invalid input"),
    INVALID_PRODUCT(404, "CTN not found"),
    INVALID_SERIAL_NUMBER(400, "Invalid Serial Number"),
    MISSING_SERIAL_NUMBER(400, "Missing Serial Number"),
    INVALID_SERIAL_NUMBER_FORMAT(400, "Invalid Serial Number Format"),
    INVALID_PURCHASE_DATE(400, "Invalid Purchase Date"),
    NO_INTERNET_CONNECTION(501, "No internet connection available"),
    REQUEST_TIME_OUT(504, "Time out Exception"),
    INVALID_DATE(1, "Invalid date"),
    USER_NOT_SIGNED_IN(2, "User not signed in"),
    METADATA_FAILED(3, "Failed to get Metadata");

    private final int id;
    private final String description;

    ErrorType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public static ErrorType fromId(final int id) {
        final ErrorType[] values = ErrorType.values();

        for (ErrorType item : values) {
            if (id == item.getCode()) {
                return item;
            }
        }

        return UNKNOWN;
    }

    public static ErrorType fromValue(final String value) {
        final ErrorType[] values = ErrorType.values();

        for (ErrorType item : values) {
            if (value.equals(item.getDescription())) {
                return item;
            }
        }

        return UNKNOWN;
    }

    public int getCode() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
