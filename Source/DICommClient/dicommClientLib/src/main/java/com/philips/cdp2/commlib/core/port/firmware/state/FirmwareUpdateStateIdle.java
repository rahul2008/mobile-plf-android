/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public class FirmwareUpdateStateIdle extends FirmwareUpdateState {

    public FirmwareUpdateStateIdle(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        if (previousState == null) {
            firmwareUpdateOperation.requestStateDownloading();
            firmwareUpdateOperation.waitForNextState();
        } else {
            if (!(previousState instanceof FirmwareUpdateStateError)) {
                firmwareUpdateOperation.onDeployFinished();
            }
            firmwareUpdateOperation.finish();
        }
    }

    @Override
    public void onError(final String message) {
        firmwareUpdateOperation.onDownloadFailed("Could not start uploading: " + message);
        firmwareUpdateOperation.finish();
    }
}
