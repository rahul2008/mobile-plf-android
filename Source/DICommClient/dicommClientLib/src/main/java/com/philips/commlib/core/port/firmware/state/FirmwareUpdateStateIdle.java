/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

public class FirmwareUpdateStateIdle extends FirmwareUpdateState {

    public FirmwareUpdateStateIdle(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        if (previousState == null) {
            operation.requestStateDownloading();
            try {
                operation.waitForNextState();
            } catch (StateWaitException e) {
                operation.onDownloadFailed("Could not start uploading.");
                operation.finish();
            }
            return;
        }

        if (!(previousState instanceof FirmwareUpdateStateError)) {
            operation.onDeployFinished();
        }
        operation.finish();
    }
}
