/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port.common.firmware;

class FirmwareUpdateIdle implements FirmwareUpdateState {
    @Override
    public void update(FirmwareUpdatePushLocal firmwareUpdateContext) {

    }

    @Override
    public void update(FirmwareUpdatePushLocal firmwareUpdateContext, byte[] firmware) {

    }

    @Override
    public void cancel(FirmwareUpdatePushLocal firmwareUpdateContext) {

    }
}
