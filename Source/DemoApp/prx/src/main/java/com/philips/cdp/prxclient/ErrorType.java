package com.philips.cdp.prxclient;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ErrorType {

    UNKNOWN(-1, "unknown"),
    ACCESS_TOKEN_INVALID(403, "malformed access token"),
    ACCESS_TOKEN_EXPIRED(500, "access token expired"),
    INVALID_VALIDATION(422, "Invalid input"),
    INVALID_PRODUCT(404, "CTN not found");

    private final int id;
    private final String description;

    ErrorType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public static ErrorType fromId(final int id) {
        final ErrorType[] values = ErrorType.values();

        for (ErrorType item : values) {
            if (id == item.getId()) {
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

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
