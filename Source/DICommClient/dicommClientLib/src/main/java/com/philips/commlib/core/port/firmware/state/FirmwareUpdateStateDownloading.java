/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

import java.io.IOException;

public class FirmwareUpdateStateDownloading extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateDownloading(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        try {
            operation.pushData();
            operation.onDownloadProgress(100);
        } catch (IOException e) {
            operation.onDownloadFailed("Could not upload firmware.");
            operation.finish();
        }

        try {
            operation.waitForNextState();
        } catch (StateWaitException e) {
            operation.onDownloadFailed("Could not upload firmware.");
            operation.finish();
        }
    }
}
