/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved. 
 */
package com.philips.commlib.core.port.firmware.operation;

import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.concurrent.ExecutorService;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdatePushLocalTest {

    @Mock
    private FirmwarePort mockFirmwarePort;

    @Mock
    private CommunicationStrategy mockCommunicationStrategy;

    @Mock
    private FirmwarePortListener mockListener;

    @Mock
    private FirmwarePortProperties mockPortProperties;

    @Captor
    private ArgumentCaptor<FirmwarePortListener.FirmwarePortException> exceptionArgumentCaptor;

    private byte[] firmwaredata = {0xC, 0x0, 0xF, 0xF, 0xE, 0xE};

    private FirmwareUpdatePushLocal operationUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        operationUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);
    }

    @Test
    public void whenDownloadFailedCallsListenerWithPortMessage() {
        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);
        when(mockPortProperties.getStatusMessage()).thenReturn("TestError");

        operationUnderTest.onDownloadFailed();

        verify(mockListener).onDownloadFailed(exceptionArgumentCaptor.capture());
        assertEquals("TestError", exceptionArgumentCaptor.getValue().getMessage());
    }
}