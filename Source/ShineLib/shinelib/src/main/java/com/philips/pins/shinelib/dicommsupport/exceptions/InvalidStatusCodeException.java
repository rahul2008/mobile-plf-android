/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport.exceptions;

public class InvalidStatusCodeException extends RuntimeException {
    public InvalidStatusCodeException() {
    }

    public InvalidStatusCodeException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidStatusCodeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidStatusCodeException(Throwable throwable) {
        super(throwable);
    }

    public InvalidStatusCodeException(Exception e) {
        super(e);
    }
}
