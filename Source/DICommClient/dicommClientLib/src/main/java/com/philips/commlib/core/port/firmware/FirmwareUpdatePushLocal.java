/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware;

import android.support.annotation.NonNull;

import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import java.util.concurrent.ExecutorService;

class FirmwareUpdatePushLocal implements FirmwareUpdateOperation {

    private static final String TAG = "FirmwareUpdatePushLocal";
    private static final long TIMEOUT_MILLIS = 30000L;

    @NonNull
    private final FirmwarePort firmwarePort;
    @NonNull
    private final byte[] firmwareData;

    private final ExecutorService executor;
    private final FirmwarePortStateWaiter firmwarePortStateWaiter;

    private FirmwareUpdateOperationState state;

    FirmwareUpdatePushLocal(@NonNull final ExecutorService executor, @NonNull final FirmwarePort firmwarePort, @NonNull byte[] firmwareData) {
        this.executor = executor;
        this.firmwarePort = firmwarePort;

        if (firmwareData.length == 0) {
            throw new IllegalArgumentException("Firmware data has zero length.");
        }
        this.firmwareData = firmwareData;
        this.firmwarePortStateWaiter = new FirmwarePortStateWaiter(this.executor, this.firmwarePort);

        // TODO check device state first

        setState(new FirmwareUpdateOperationIdle(this));
    }

    void setState(@NonNull FirmwareUpdateOperationState state) {
        this.state = state;
    }

    FirmwarePortState updateFirmwarePortState(@NonNull final FirmwarePortState state) {
        this.firmwarePort.putProperties(FirmwarePortKey.STATE.toString(), state.toString());

        return firmwarePortStateWaiter.await(state, TIMEOUT_MILLIS);
    }

    @Override
    public void execute() {
        this.state.execute();
    }

    @Override
    public void cancel() {
        this.state.cancel();
        this.firmwarePort.finishFirmwareUpdateOperation();
    }
}
