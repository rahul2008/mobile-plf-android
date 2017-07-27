/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware;

import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateState;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUpdateException;

public interface FirmwareUpdate {

    void start(@Nullable FirmwareUpdateState previousState);

    void deploy() throws FirmwareUpdateException;

    void cancel() throws FirmwareUpdateException;

    void finish();

    void onError(String message);
}
