/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.util;

public class FirmwareUpdateException extends Exception {
    public FirmwareUpdateException(String reason) {
        super(reason);
    }
}
