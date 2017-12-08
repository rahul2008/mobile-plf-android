/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport.exceptions;

public class InvalidMessageTerminationException extends RuntimeException {
    public InvalidMessageTerminationException() {
    }

    public InvalidMessageTerminationException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidMessageTerminationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidMessageTerminationException(Throwable throwable) {
        super(throwable);
    }

    public InvalidMessageTerminationException(Exception e) {
        super(e);
    }
}
