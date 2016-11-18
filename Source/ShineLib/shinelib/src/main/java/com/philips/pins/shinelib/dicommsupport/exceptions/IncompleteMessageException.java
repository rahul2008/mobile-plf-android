/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport.exceptions;

public class IncompleteMessageException extends RuntimeException {
    public IncompleteMessageException() {
    }

    public IncompleteMessageException(String detailMessage) {
        super(detailMessage);
    }

    public IncompleteMessageException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IncompleteMessageException(Throwable throwable) {
        super(throwable);
    }

    public IncompleteMessageException(Exception e) {
        super(e);
    }
}
