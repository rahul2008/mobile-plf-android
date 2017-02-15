/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.port.firmware.FirmwareUpdate;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

public abstract class FirmwareUpdateState implements FirmwareUpdate {

    protected final String TAG = getClass().getSimpleName();

    protected final FirmwareUpdatePushLocal operation;

    public FirmwareUpdateState(@NonNull FirmwareUpdatePushLocal operation) {
        this.operation = operation;
    }

    @Override
    public void start() {

    }

    public void start(@Nullable FirmwareUpdateState previousState) {
        DICommLog.d(DICommLog.FIRMWAREPORT, ">>> Started state [" + TAG + "]");
        onStart(previousState);
    }

    public void cancel() throws FirmwareUpdateException {
        throw new FirmwareUpdateException("Cancel not allowed in state [" + TAG + "]");
    }

    public void deploy() throws FirmwareUpdateException {
        throw new FirmwareUpdateException("Deploying not allowed in state [" + TAG + "]");
    }

    public void finish() {
        onFinish();
        DICommLog.d(DICommLog.FIRMWAREPORT, "<<< Finished state [" + TAG + "]");
    }

    protected abstract void onStart(@Nullable FirmwareUpdateState previousState);

    protected abstract void onFinish();
}
