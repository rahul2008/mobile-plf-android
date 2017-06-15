/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.exception;

public final class TransportUnavailableException extends Exception {
    public TransportUnavailableException(String message) {
        super(message);
    }

    public TransportUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
