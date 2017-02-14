/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateChecking extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateChecking(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void execute(FirmwareUpdateState previousState) {
        operation.waitForNextState();
    }

    @Override
    public void onStateEnd() {
        operation.onProgress(100);
    }
}
