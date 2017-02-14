/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateCanceling extends FirmwareUpdateState {

    public FirmwareUpdateStateCanceling(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void start(FirmwareUpdateState previousState) {
        operation.waitForNextState();
    }
}
