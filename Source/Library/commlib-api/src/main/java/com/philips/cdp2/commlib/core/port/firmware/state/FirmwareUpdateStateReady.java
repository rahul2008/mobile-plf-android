/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;

public class FirmwareUpdateStateReady extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateReady(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        firmwareUpdateOperation.onDownloadFinished();
    }

    @Override
    public void deploy() {
        firmwareUpdateOperation.requestState(PROGRAMMING);
        firmwareUpdateOperation.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        super.onError("Deployment failed: " + message);
    }
}
