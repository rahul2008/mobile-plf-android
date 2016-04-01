package com.philips.cdp.prodreg.handler;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum PRXRequestType {
    METADATA(0), REGISTRATION(1), GET_REGISTERED_PRODUCTS(2);
    private final int value;

    PRXRequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
