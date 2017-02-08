/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port.common.firmware;

import android.support.annotation.NonNull;

class FirmwareUpdatePushLocal implements FirmwareUpdateOperation {
    private FirmwareUpdateState firmwareUpdateState;

    FirmwareUpdatePushLocal(byte[] firmwareData) {
        setFirmwareUpdateState(new FirmwareUpdateIdle());
    }

    void setFirmwareUpdateState(@NonNull FirmwareUpdateState firmwareUpdateState) {
        this.firmwareUpdateState = firmwareUpdateState;
    }

    @Override
    public void cancel() {
        this.firmwareUpdateState.cancel(this);
    }
}
