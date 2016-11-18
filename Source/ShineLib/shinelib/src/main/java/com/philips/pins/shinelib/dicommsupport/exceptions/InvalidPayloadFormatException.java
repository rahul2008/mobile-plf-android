/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport.exceptions;

public class InvalidPayloadFormatException extends RuntimeException {
    public InvalidPayloadFormatException() {
    }

    public InvalidPayloadFormatException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidPayloadFormatException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidPayloadFormatException(Throwable throwable) {
        super(throwable);
    }

    public InvalidPayloadFormatException(Exception e) {
        super(e);
    }
}
