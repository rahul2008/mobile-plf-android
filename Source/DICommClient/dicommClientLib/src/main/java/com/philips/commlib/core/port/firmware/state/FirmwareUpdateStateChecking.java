/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

public class FirmwareUpdateStateChecking extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateChecking(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        firmwareUpdate.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        super.onError("Error while checking firmware: " + message);
    }

    @Override
    public void onFinish() {
        firmwareUpdate.onCheckingProgress(100);
    }
}
