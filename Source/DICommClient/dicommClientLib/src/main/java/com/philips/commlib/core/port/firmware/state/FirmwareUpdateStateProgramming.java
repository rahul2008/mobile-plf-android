/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

public class FirmwareUpdateStateProgramming extends FirmwareUpdateState {

    public FirmwareUpdateStateProgramming(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        firmwareUpdate.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        firmwareUpdate.onDeployFailed("Deployment failed: " + message);
        firmwareUpdate.finish();
    }
}
