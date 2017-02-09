/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import java.util.concurrent.ExecutorService;

class FirmwareUpdatePushLocal implements FirmwareUpdateOperation {

    @NonNull
    private final FirmwarePort firmwarePort;
    @NonNull
    private final byte[] firmwareData;

    private FirmwareUpdateOperationState firmwareUpdateOperationState;
    private ExecutorService executor;

    FirmwareUpdatePushLocal(@NonNull final ExecutorService executor, @NonNull final FirmwarePort firmwarePort, @NonNull byte[] firmwareData) {
        this.executor = executor;
        this.firmwarePort = firmwarePort;

        if (firmwareData.length == 0) {
            throw new IllegalArgumentException("Firmware data has zero length.");
        }
        this.firmwareData = firmwareData;

        setFirmwareUpdateOperationState(new FirmwareUpdateOperationIdle(this));
    }

    void setFirmwareUpdateOperationState(@NonNull FirmwareUpdateOperationState firmwareUpdateOperationState) {
        this.firmwareUpdateOperationState = firmwareUpdateOperationState;
    }

    void setFirmwarePortState(@NonNull final FirmwarePortState state) {
        FirmwarePortStateWaiter waiter = new FirmwarePortStateWaiter(this.executor, this.firmwarePort);
        waiter.await(state, 5000L);


        this.firmwarePort.putProperties(FirmwarePortKey.STATE.toString(), state.toString());
    }

    @Override
    public void finish() {
        this.firmwarePort.finishFirmwareUpdateOperation();
    }

    @Override
    public void cancel() {
        this.firmwareUpdateOperationState.cancel();
    }
}
