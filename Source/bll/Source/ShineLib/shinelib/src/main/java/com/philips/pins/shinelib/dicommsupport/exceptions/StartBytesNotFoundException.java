/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport.exceptions;

public class StartBytesNotFoundException extends RuntimeException {
    public StartBytesNotFoundException() {
    }

    public StartBytesNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public StartBytesNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public StartBytesNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public StartBytesNotFoundException(Exception e) {
        super(e);
    }
}
