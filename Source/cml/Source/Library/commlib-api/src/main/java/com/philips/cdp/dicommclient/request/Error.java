/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

/**
 * Definition of error messages used in this library
 * @publicApi
 */
public enum Error {

    NOT_UNDERSTOOD("Request not understood."),
    REQUEST_FAILED("Failed to perform request."),
    INVALID_PARAMETER("Invalid parameter."),
    NO_SUCH_METHOD("No such method."),
    NO_SUCH_OPERATION("No such operation."),
    NO_SUCH_PORT("No such port."),
    NO_SUCH_PRODUCT("No such product."),
    NO_SUCH_PROPERTY("No such property."),
    NOT_IMPLEMENTED("Not implemented."),
    NOT_SUBSCRIBED("Not subscribed."),
    OUT_OF_MEMORY("Out of memory."),
    PROPERTY_ALREADY_EXISTS("Property already exists."),
    PROTOCOL_VIOLATION("Protocol violation."),
    UNKNOWN("Unknown error."),
    VERSION_NOT_SUPPORTED("Version not supported."),
    WRONG_PARAMETERS("Wrong parameters."),

    CANNOT_CONNECT("Cannot connect to appliance."),
    IOEXCEPTION("I/O exception occurred."),
    NO_REQUEST_DATA("Request cannot be performed with null or empty data."),
    NO_TRANSPORT_AVAILABLE("Request cannot be performed - No transport available."),
    NOT_CONNECTED("Request cannot be performed - Not connected to an appliance."),
    TIMED_OUT("Request timed out"),
    NOT_AVAILABLE("Communication not available"),
    INSECURE_CONNECTION("Connection is not secure");

    private final String mErrorMessage;

    Error(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    /**
     * Returns the error messages associated to the Error as a String.
     * @return String The error message
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
