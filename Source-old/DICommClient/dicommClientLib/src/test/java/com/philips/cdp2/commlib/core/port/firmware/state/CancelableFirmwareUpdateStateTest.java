/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CancelableFirmwareUpdateStateTest {

    @Mock
    private FirmwareUpdatePushLocal mockFirmwareUpdate;

    private CancelableFirmwareUpdateState stateUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        stateUnderTest = new CancelableFirmwareUpdateState(mockFirmwareUpdate) {
            @Override
            protected void onStart(@Nullable final FirmwareUpdateState previousState) {
                // Ignored
            }
        };
    }

    @Test
    public void onCancel_requestStateCancel() {
        stateUnderTest.cancel();

        verify(mockFirmwareUpdate).requestState(CANCELING);
    }

    @Test
    public void onCancelStateChangeError_downloadFailed() {
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                stateUnderTest.onError("A specific error message.");
                return null;
            }
        }).when(mockFirmwareUpdate).waitForNextState();

        stateUnderTest.cancel();

        verify(mockFirmwareUpdate).onDownloadFailed(anyString());
    }
}
