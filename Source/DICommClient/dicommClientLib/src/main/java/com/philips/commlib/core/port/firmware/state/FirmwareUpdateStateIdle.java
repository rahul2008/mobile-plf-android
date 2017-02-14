/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;

public class FirmwareUpdateStateIdle extends FirmwareUpdateState {

    public FirmwareUpdateStateIdle(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void execute(FirmwareUpdateState previousState) {
        if (previousState == null) {
            operation.requestState(DOWNLOADING);
            operation.waitForNextState();
            return;
        }

        if (!(previousState instanceof FirmwareUpdateStateError)) {
            operation.onDeployFinished();
        }
        operation.onOperationFinished();
    }
}
