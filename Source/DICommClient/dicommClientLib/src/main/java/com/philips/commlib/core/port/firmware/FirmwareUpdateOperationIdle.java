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
        this.operation.setFirmwarePortState(FirmwarePortState.DOWNLOADING);
    }

    @Override
    public void cancel() {
        this.operation.finish();
    }
}
