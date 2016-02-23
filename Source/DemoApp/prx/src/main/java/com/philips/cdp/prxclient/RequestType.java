package com.philips.cdp.prxclient;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface RequestType {
    int DEPRECATED_GET_OR_POST = -1;
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int HEAD = 4;
    int OPTIONS = 5;
    int TRACE = 6;
    int PATCH = 7;
}
