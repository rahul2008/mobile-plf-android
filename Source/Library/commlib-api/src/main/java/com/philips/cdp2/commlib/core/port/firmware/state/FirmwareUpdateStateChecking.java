/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateChecking extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateChecking(@NonNull FirmwareUpdatePushLocal firmwareUpdateOperation) {
        super(firmwareUpdateOperation);
    }

    @Override
    public void onStart(@Nullable FirmwareUpdateState previousState) {
        firmwareUpdateOperation.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        super.onError("Error while checking firmware: " + message);
    }

    @Override
    public void onFinish() {
        firmwareUpdateOperation.onCheckingProgress(100);
    }
}
