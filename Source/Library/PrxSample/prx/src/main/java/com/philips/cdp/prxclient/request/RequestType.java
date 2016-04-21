package com.philips.cdp.prxclient.request;



/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum RequestType {

    GET(0),
    POST(1);

    private int value;


    RequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
