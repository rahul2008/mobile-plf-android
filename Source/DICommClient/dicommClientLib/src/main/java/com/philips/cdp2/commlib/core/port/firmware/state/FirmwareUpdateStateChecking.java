/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateChecking extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateChecking(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
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
