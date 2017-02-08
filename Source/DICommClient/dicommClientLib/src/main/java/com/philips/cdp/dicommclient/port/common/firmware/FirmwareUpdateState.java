/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port.common.firmware;

interface FirmwareUpdateState {
    void update(FirmwareUpdatePushLocal firmwareUpdateContext);

    void update(FirmwareUpdatePushLocal firmwareUpdateContext, byte[] firmware);

    void cancel(FirmwareUpdatePushLocal firmwareUpdateContext);
}
