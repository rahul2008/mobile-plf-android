package com.philips.cdp.backend;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum PRXRequestType {
    METADATA(0), REGISTRATION(1), FETCH_PRODUCTS(2);
    private int value;

    PRXRequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
