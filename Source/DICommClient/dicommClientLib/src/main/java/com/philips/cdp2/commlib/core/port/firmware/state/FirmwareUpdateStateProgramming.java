/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateProgramming extends FirmwareUpdateState {

    public FirmwareUpdateStateProgramming(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        firmwareUpdateOperation.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        firmwareUpdateOperation.onDeployFailed("Deployment failed: " + message);
        firmwareUpdateOperation.finish();
    }
}
