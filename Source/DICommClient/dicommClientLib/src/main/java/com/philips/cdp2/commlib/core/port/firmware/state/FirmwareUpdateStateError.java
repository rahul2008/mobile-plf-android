/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;

public class FirmwareUpdateStateError extends FirmwareUpdateState {
    public FirmwareUpdateStateError(@NonNull FirmwareUpdatePushLocal firmwareUpdate) {
        super(firmwareUpdate);
    }

    @Override
    public void onStart(FirmwareUpdateState previousState) {
        if (previousState instanceof FirmwareUpdateStatePreparing ||
                previousState instanceof FirmwareUpdateStateDownloading ||
                previousState instanceof FirmwareUpdateStateChecking) {
            firmwareUpdateOperation.onDownloadFailed();
        }

        if (previousState instanceof FirmwareUpdateStateReady ||
                previousState instanceof FirmwareUpdateStateProgramming) {
            firmwareUpdateOperation.onDeployFailed();
        }

        firmwareUpdateOperation.requestState(IDLE);
        firmwareUpdateOperation.waitForNextState();
    }

    @Override
    public void onError(final String message) {
        DICommLog.e(DICommLog.FIRMWAREPORT, "Reached error state - reason: " + message);
        firmwareUpdateOperation.finish();
    }
}
