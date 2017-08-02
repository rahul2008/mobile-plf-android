/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.operation;

import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUpdateException;

public interface FirmwareUpdateOperation {

    void start(long stateTransitionTimeoutMillis);

    void deploy(long stateTransitionTimeoutMillis) throws FirmwareUpdateException;

    void cancel(long stateTransitionTimeoutMillis) throws FirmwareUpdateException;

    void finish();
}
