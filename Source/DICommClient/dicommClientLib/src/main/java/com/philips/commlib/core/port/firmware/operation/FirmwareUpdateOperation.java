/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.operation;

public interface FirmwareUpdateOperation {
    void execute();

    void cancel();
}
