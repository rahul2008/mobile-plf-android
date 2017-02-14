/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

public class FirmwareUpdateStateProgramming extends FirmwareUpdateState {

    public FirmwareUpdateStateProgramming(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        try {
            operation.waitForNextState();
        } catch (StateWaitException e) {
            operation.onDeployFailed("Deployment failed.");
            operation.onFinish();
        }
    }

    @Override
    protected void onFinish() {

    }
}
