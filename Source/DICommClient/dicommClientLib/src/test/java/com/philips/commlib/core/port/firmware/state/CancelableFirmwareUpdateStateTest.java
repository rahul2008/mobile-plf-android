/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import android.support.annotation.Nullable;

import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.util.StateWaitException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CancelableFirmwareUpdateStateTest {

    @Mock
    private FirmwareUpdatePushLocal mockOperation;

    private CancelableFirmwareUpdateState stateUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        stateUnderTest = new CancelableFirmwareUpdateState(mockOperation) {
            @Override
            protected void onStart(@Nullable final FirmwareUpdateState previousState) {

            }
        };
    }

    @Test
    public void onCancel_requestStateCancel() {
        stateUnderTest.cancel();

        verify(mockOperation).requestState(CANCELING);
    }

    @Test
    public void onCancelStateChangeError_downloadFailed() throws StateWaitException {
        doThrow(new StateWaitException("")).when(mockOperation).waitForNextState();

        stateUnderTest.cancel();

        verify(mockOperation).onDownloadFailed();
    }
}