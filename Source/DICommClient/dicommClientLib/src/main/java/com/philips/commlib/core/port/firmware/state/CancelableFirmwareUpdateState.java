/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;

public abstract class CancelableFirmwareUpdateState extends FirmwareUpdateState {

    public CancelableFirmwareUpdateState(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void cancel() {
        operation.requestState(CANCELING);
        operation.waitForNextState();
    }
}
