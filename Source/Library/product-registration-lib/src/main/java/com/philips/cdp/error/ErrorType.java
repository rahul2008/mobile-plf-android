package com.philips.cdp.error;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ErrorType {

    UNKNOWN(-1, "unknown"),
    ACCESS_TOKEN_INVALID(403, "malformed access token"),
    USER_TOKEN_EXPIRED(500, "User access token has expired"),
    REFRESH_ACCESS_TOKEN_FAILED(500, "error in getting access token"),
    INVALID_VALIDATION(422, "Input validation Error"),
    INVALID_CTN(404, "Enter valid CTN number"),
    INVALID_SERIALNUMBER(400, "Enter valid Serial number"),
    MISSING_SERIALNUMBER(400, "Required serial number is missing"),
    MISSING_DATE(1, "Required date field is missing"),
    INVALID_DATE(400, "Date should be less than year 2000"),
    NO_INTERNET_AVAILABLE(501, "No internet connection available"),
    INTERNAL_SERVER_ERROR(504, "Internal server error"),
    USER_NOT_SIGNED_IN(2, "User not signed in"),
    PRODUCT_ALREADY_REGISTERED(2, "Product already registered"),
    FETCH_REGISTERED_PRODUCTS_FAILED(2, "Failed to fetch registered products"),
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
