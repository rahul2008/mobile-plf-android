/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.port.firmware.FirmwareUpdate;
import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUpdateException;

public abstract class FirmwareUpdateState implements FirmwareUpdate {

    protected final String TAG = getClass().getSimpleName();

    protected final FirmwareUpdatePushLocal firmwareUpdateOperation;

    public FirmwareUpdateState(@NonNull FirmwareUpdatePushLocal firmwareUpdateOperation) {
        this.firmwareUpdateOperation = firmwareUpdateOperation;
    }

    @Override
    public void start(@Nullable FirmwareUpdateState previousState) {
        DICommLog.d(DICommLog.FIRMWAREPORT, ">>> Started state [" + TAG + "]");
        onStart(previousState);
    }

    @Override
    public void cancel() throws FirmwareUpdateException {
        throw new FirmwareUpdateException("Cancel not allowed in state [" + TAG + "]");
    }

    @Override
    public void deploy() throws FirmwareUpdateException {
        throw new FirmwareUpdateException("Deploying not allowed in state [" + TAG + "]");
    }

    @Override
    public void finish() {
        onFinish();
        DICommLog.d(DICommLog.FIRMWAREPORT, "<<< Finished state [" + TAG + "]");
    }

    protected abstract void onStart(@Nullable FirmwareUpdateState previousState);

    protected void onFinish() {
    }
}
