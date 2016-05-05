package com.philips.cdp.prxclient.error;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PrxError {
    private String description;
    private int statusCode;

    public PrxError(String description, int statusCode) {
        this.description = description;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }
}
