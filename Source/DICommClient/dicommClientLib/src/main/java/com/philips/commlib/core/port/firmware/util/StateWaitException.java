/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.util;

public class StateWaitException extends Exception {
    public StateWaitException(Exception cause) {
        super(cause);
    }
}
