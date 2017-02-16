/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved. 
 */
package com.philips.commlib.core.port.firmware.state;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

abstract public class WaitingFirmwareUpdateState {

    @Mock
    FirmwareUpdatePushLocal mockOperation;

    FirmwareUpdateState stateUnderTest;

    @Test
    public void onStart_waitForNextState() throws StateWaitException {
        stateUnderTest.start(null);

        verify(mockOperation).waitForNextState();
    }

    @Test
    public void onStartStateChangeError_downloadFailed() throws StateWaitException {
        doThrow(new StateWaitException("")).when(mockOperation).waitForNextState();

        stateUnderTest.start(null);

        verify(mockOperation).onDownloadFailed(anyString());
    }

    @Test
    public void onStartStateChangeError_finishOperation() throws StateWaitException {
        doThrow(new StateWaitException("")).when(mockOperation).waitForNextState();

        stateUnderTest.start(null);

        verify(mockOperation).finish();
    }
}
