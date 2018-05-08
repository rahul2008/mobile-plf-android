/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved. 
 */
package com.philips.cdp2.commlib.core.port.firmware.operation;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateDownloading;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateError;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateIdle;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirmwareUpdatePushLocal.class})
public class FirmwareUpdatePushLocalTest {

    @Mock
    private FirmwarePort mockFirmwarePort;

    @Mock
    private CommunicationStrategy mockCommunicationStrategy;

    @Mock
    private FirmwarePortListener mockListener;

    @Mock
    private FirmwarePortProperties mockPortProperties;


    @Mock private FirmwareUpdateStateIdle mockIdleState;
    @Mock private FirmwareUpdateStateDownloading mockDownloadingState;
    @Mock private FirmwareUpdateStateError mockErrorState;

    @Captor
    private ArgumentCaptor<FirmwarePortListener.FirmwarePortException> exceptionArgumentCaptor;

    private byte[] firmwaredata = {0xC, 0x0, 0xF, 0xF, 0xE, 0xE};

    private FirmwareUpdatePushLocal firmwareUpdateUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DICommLog.disableLogging();

        when(mockPortProperties.getState()).thenReturn(FirmwarePortProperties.FirmwarePortState.IDLE);
        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);

        PowerMockito.whenNew(FirmwareUpdateStateIdle.class).withAnyArguments().thenReturn(mockIdleState);
        PowerMockito.whenNew(FirmwareUpdateStateDownloading.class).withAnyArguments().thenReturn(mockDownloadingState);
        PowerMockito.whenNew(FirmwareUpdateStateError.class).withAnyArguments().thenReturn(mockErrorState);

        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);
    }

    @Test
    public void givenPushLocalIsInitialized_whenRemoteIsInIdleState_thenStateIsIdle() {
        assertTrue(firmwareUpdateUnderTest.getState() instanceof FirmwareUpdateStateIdle);
    }

    @Test
    public void givenPushLocalIsInitialized_whenRemoteIsInUnknownState_thenStateIsIdle() {
        when(mockFirmwarePort.getPortProperties()).thenReturn(null);

        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);

        assertTrue(firmwareUpdateUnderTest.getState() instanceof FirmwareUpdateStateIdle);
    }

    @Test
    public void givenPushLocalIsInitialized_whenRemoteIsInDownloadingState_thenStateIsDownloading() {
        when(mockPortProperties.getState()).thenReturn(FirmwarePortProperties.FirmwarePortState.DOWNLOADING);

        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);

        assertTrue(firmwareUpdateUnderTest.getState() instanceof FirmwareUpdateStateDownloading);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenPushLocalIsInitialized_whenFirmwareDataIsEmpty_thenExceptionIsThrown() {
        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, new byte[0]);
    }

    @Test
    public void givenRemoteReportsAnError_whenDownloadHasFailed_thenFailureIsReportedWithMessage() {
        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);
        when(mockPortProperties.getStatusMessage()).thenReturn("TestError");

        firmwareUpdateUnderTest.onDownloadFailed();

        verify(mockListener).onDownloadFailed(exceptionArgumentCaptor.capture());
        assertEquals("TestError", exceptionArgumentCaptor.getValue().getMessage());
    }
}
