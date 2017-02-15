/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

public class FirmwareUpdateStatePreparing extends CancelableFirmwareUpdateState {

    public FirmwareUpdateStatePreparing(@NonNull FirmwareUpdatePushLocal operation) {
        super(operation);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        try {
            operation.waitForNextState();
        } catch (StateWaitException e) {
            operation.onDownloadFailed("Could not upload firmware.");
            operation.finish();
        }
    }
}
