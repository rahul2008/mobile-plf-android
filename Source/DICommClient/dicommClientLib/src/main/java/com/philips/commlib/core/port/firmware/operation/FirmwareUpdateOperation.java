/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.operation;

import com.philips.commlib.core.port.firmware.state.IncompatibleStateException;

public interface FirmwareUpdateOperation {
    void start();

    void deploy() throws IncompatibleStateException;

    void cancel() throws IncompatibleStateException;
}
