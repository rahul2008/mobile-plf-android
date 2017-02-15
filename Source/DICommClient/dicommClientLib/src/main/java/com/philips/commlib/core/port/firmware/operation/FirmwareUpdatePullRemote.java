/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.operation;

import com.philips.commlib.core.port.firmware.FirmwareUpdate;

public class FirmwareUpdatePullRemote implements FirmwareUpdate {

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void deploy() throws FirmwareUpdateException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void cancel() throws FirmwareUpdateException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void finish() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
