/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

public class IncompatibleStateException extends Exception {
    public IncompatibleStateException(String reason) {
        super(reason);
    }
}
