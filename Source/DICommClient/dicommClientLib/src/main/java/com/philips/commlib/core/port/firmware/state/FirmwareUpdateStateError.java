/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;

public class FirmwareUpdateStateError extends FirmwareUpdateState {
    public FirmwareUpdateStateError(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void start(FirmwareUpdateState previousState) {
        if (previousState instanceof FirmwareUpdateStatePreparing ||
                previousState instanceof FirmwareUpdateStateDownloading ||
                previousState instanceof FirmwareUpdateStateChecking) {
            operation.onDownloadFailed();
        }

        if (previousState instanceof FirmwareUpdateStateReady ||
                previousState instanceof FirmwareUpdateStateProgramming) {
            operation.onDeployFailed();
        }

        operation.requestState(IDLE);
        operation.waitForNextState();
    }
}
