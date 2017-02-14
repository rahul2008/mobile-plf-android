/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import java.io.IOException;

public class FirmwareUpdateStateDownloading extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStateDownloading(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void start(FirmwareUpdateState previousState) {
        try {
            operation.pushData();
            operation.onDownloadProgress(100);
        } catch (IOException e) {
            // TODO inform UI, stop operation!
        }
        operation.waitForNextState();
    }
}
