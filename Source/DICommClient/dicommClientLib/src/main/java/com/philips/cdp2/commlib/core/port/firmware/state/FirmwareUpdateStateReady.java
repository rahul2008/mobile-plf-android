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
        firmwareUpdate.onDownloadFinished();
    }

    @Override
    public void deploy() {
        firmwareUpdate.requestState(PROGRAMMING);
        firmwareUpdate.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        firmwareUpdate.onDeployFailed("Deployment failed.");
        firmwareUpdate.finish();
    }
}
