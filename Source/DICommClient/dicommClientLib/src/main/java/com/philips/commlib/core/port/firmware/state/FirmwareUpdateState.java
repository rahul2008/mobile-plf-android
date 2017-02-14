/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public abstract class FirmwareUpdateState {

    protected final FirmwareUpdatePushLocal operation;

    public FirmwareUpdateState(@NonNull FirmwareUpdatePushLocal operation) {
        this.operation = operation;
    }

    public abstract void execute(@Nullable FirmwareUpdateState previousState);

    public void cancel() {
        throw new UnsupportedOperationException("Canceling not allowed in this state.");
    }

    public void deploy() {
        throw new UnsupportedOperationException("Deploying not allowed in this state.");
    }

    public void onStateEnd() {

    }
}
