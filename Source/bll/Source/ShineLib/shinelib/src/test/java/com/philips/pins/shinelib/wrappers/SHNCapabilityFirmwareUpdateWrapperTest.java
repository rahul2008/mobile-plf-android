package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityFirmwareUpdateWrapperTest {

    private SHNCapabilityFirmwareUpdateWrapper wrapper;

    @Mock
    private Handler userHandlerMock;

    @Mock
    private Handler internalHandlerMock;

    @Mock
    private SHNCapabilityFirmwareUpdate capabilityMock;

    private byte[] data = "Jeroen rocks".getBytes();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(internalHandlerMock).post(any());

        wrapper = new SHNCapabilityFirmwareUpdateWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void whenCallingUploadFirmwareWithoutResume_thenTheCallIsForwardedWithCorrectParams() throws Exception {

        wrapper.uploadFirmware(data, false);

        verify(capabilityMock).uploadFirmware(data, false);
    }

    @Test
    public void whenCallingUploadFirmwareWithResume_thenTheCallIsForwardedWithCorrectParams() throws Exception {

        wrapper.uploadFirmware(data, true);

        verify(capabilityMock).uploadFirmware(data, true);
    }
}