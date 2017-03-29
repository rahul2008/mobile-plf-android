/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;

abstract class CancelableFirmwareUpdateState extends FirmwareUpdateState {

    CancelableFirmwareUpdateState(@NonNull FirmwareUpdatePushLocal firmwareUpdateOperation) {
        super(firmwareUpdateOperation);
    }

    @Override
    public void cancel() {
        firmwareUpdateOperation.requestState(CANCELING);
        firmwareUpdateOperation.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        firmwareUpdateOperation.onDownloadFailed(message);
        firmwareUpdateOperation.finish();
    }
}
