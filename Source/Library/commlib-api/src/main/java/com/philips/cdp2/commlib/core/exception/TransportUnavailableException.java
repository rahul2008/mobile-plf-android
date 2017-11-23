/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.exception;

/**
 * This error is thrown whenever the underlying transport is unavailable.
 *
 * @publicApi
 */
public final class TransportUnavailableException extends RuntimeException {

    /**
     * Construct a new {@link TransportUnavailableException} with a message.
     *
     * @param message message.
     * @see RuntimeException#RuntimeException(String)
     */
    public TransportUnavailableException(String message) {
        super(message);
    }

    /**
     * Construct a new {@link TransportUnavailableException} with a message and a {@link Throwable} cause.
     *
     * @param message message.
     * @param cause   cause.
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public TransportUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
