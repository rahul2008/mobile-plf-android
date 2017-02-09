/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

class FirmwareUpdateOperationIdle implements FirmwareUpdateOperationState {

    private final FirmwareUpdatePushLocal operation;

    FirmwareUpdateOperationIdle(@NonNull FirmwareUpdatePushLocal operation) {
        this.operation = operation;
    }

    @Override
    public void execute() {
        FirmwarePortState state = this.operation.updateFirmwarePortState(FirmwarePortState.DOWNLOADING);

        // TODO check for correct state and act
    }

    @Override
    public void cancel() {
        // Nothing to do.
    }
}
