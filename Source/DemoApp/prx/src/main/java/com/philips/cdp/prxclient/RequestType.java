package com.philips.cdp.prxclient;



/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum RequestType {

    DEPRECATED_GET_OR_POST(-1),
    GET(0),
    POST(1),
    PUT(2),
    DELETE(3),
    HEAD(4),
    OPTIONS(5),
    TRACE(6),
    PATCH(7);

    private int value;


    RequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
