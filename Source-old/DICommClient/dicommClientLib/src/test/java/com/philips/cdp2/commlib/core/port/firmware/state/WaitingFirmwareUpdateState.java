/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved. 
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

abstract public class WaitingFirmwareUpdateState {

    @Mock
    FirmwareUpdatePushLocal mockFirmwareUpdate;

    FirmwareUpdateState stateUnderTest;

    @Test
    public void onStart_waitForNextState() {
        stateUnderTest.start(null);

        verify(mockFirmwareUpdate).waitForNextState();
    }

    @Test
    public void onStartStateChangeError_downloadFailed() {
        stateUnderTest.onError("Something went wrong!");

        verify(mockFirmwareUpdate).onDownloadFailed(anyString());
    }

    @Test
    public void onStartStateChangeError_finishOperation() {
        stateUnderTest.onError("Something went wrong!");

        verify(mockFirmwareUpdate).finish();
    }
}
