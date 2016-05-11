package com.philips.cdp.prodreg.error;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum ProdRegError {

    //Defined Error code
    UNKNOWN(-1, "unknown"),
    USER_NOT_SIGNED_IN(10, "User not signed in"),
    MISSING_DATE(12, "Required date field is missing"),
    INVALID_DATE(3, "Date should be less than year 2000"),
    MISSING_SERIALNUMBER(4, "Required serial number is missing"),
    INVALID_SERIALNUMBER(5, "Enter valid Serial number"),
    PRODUCT_ALREADY_REGISTERED(6, "Product already registered"),
    FETCH_REGISTERED_PRODUCTS_FAILED(7, "Failed to fetch registered products"),
    NO_INTERNET_AVAILABLE(9, "No internet connection available"),
    INTERNAL_SERVER_ERROR(2, "Internal server error"),
    TIME_OUT(504, "Time out error"),
    //Server Error Code
    INVALID_CTN(404, "Enter valid CTN number"),
    ACCESS_TOKEN_INVALID(401, "malformed access token"),
    INVALID_VALIDATION(422, "Input validation Error");

    private final int id;
    private final String description;

    ProdRegError(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public static ProdRegError fromId(final int id) {
        final ProdRegError[] values = ProdRegError.values();

        for (ProdRegError item : values) {
            if (id == item.getCode()) {
                return item;
            }
        }

        return UNKNOWN;
    }

    public static ProdRegError fromValue(final String value) {
        final ProdRegError[] values = ProdRegError.values();

        for (ProdRegError item : values) {
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
