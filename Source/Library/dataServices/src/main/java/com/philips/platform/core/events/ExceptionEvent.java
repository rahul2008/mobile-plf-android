/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ExceptionEvent extends Event {
    private final String message;
    private final Throwable cause;

    public ExceptionEvent(final String message, final Exception cause) {
        this.message = message;
        this.cause = cause;
    }

    public ExceptionEvent(final int referenceId, final String message, final Throwable cause) {
        super(referenceId);
        this.message = message;
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }
}
