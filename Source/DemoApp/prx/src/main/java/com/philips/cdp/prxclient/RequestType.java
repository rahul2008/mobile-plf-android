package com.philips.cdp.prxclient;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum RequestType {
    GET(0),
    POST(1),
    PUT(2),
    DELETE(3),
    HEAD(4),
    OPTIONS(5),
    TRACE(6),
    PATCH(7);

    private final int method;

    RequestType(final int method) {
        this.method = method;
    }

    public static RequestType fromMethod(final int id) {
        final RequestType[] values = RequestType.values();

        for (RequestType item : values) {
            if (id == item.getMethod()) {
                return item;
            }
        }

        return POST;
    }

    public int getMethod() {
        return method;
    }

}
