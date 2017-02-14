/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;

public class FirmwareUpdateStateReady extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateReady(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void execute(FirmwareUpdateState previousState) {
        operation.onDownloadFinished();
    }

    @Override
    public void deploy() {
        operation.requestState(PROGRAMMING);
        operation.waitForNextState();
    }
}
