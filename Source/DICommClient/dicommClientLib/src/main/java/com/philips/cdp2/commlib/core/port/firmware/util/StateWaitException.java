/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.util;

public class StateWaitException extends Exception {
    public StateWaitException(Exception cause) {
        super(cause);
    }

    public StateWaitException(String reason) {
        super(reason);
    }
}
