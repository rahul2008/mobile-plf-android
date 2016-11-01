/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport.exceptions;

public class InvalidPayloadLengthException extends RuntimeException {
    public InvalidPayloadLengthException() {
    }

    public InvalidPayloadLengthException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidPayloadLengthException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidPayloadLengthException(Throwable throwable) {
        super(throwable);
    }

    public InvalidPayloadLengthException(Exception e) {
        super(e);
    }
}
