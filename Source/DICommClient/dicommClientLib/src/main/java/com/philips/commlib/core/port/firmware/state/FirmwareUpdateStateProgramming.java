/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateProgramming extends FirmwareUpdateState {

    public FirmwareUpdateStateProgramming(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void start(FirmwareUpdateState previousState) {
        // TODO do the actual programming

        operation.waitForNextState();
    }
}
