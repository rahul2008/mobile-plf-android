/*
 * Copyright (c) 2016, 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.commlib.core.exception;

public final class TransportUnavailableException extends RuntimeException {
    public TransportUnavailableException(String message) {
        super(message);
    }

    public TransportUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
