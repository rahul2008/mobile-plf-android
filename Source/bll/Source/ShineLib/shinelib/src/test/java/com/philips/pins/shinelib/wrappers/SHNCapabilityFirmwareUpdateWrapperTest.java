package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
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

    private boolean uploadFirmwareWithResumeParamCalled;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(internalHandlerMock).post(any());

        wrapper = new SHNCapabilityFirmwareUpdateWrapper(capabilityMock, internalHandlerMock, userHandlerMock) {
            @Override
            public void uploadFirmware(final byte[] firmwareData, boolean shouldResume) {
                uploadFirmwareWithResumeParamCalled = true;
                super.uploadFirmware(firmwareData, shouldResume);
            }
        };
    }

    @Test
    public void whenCallingUploadFirmwareWithoutResumeParam_thenItIsForwardedToUploadFirmwareWithResumeBeingFalse() throws Exception {

        wrapper.uploadFirmware(data);

        assertThat(uploadFirmwareWithResumeParamCalled).isTrue();
    }

    @Test
    public void whenCallingUploadFirmwareWithoutResume_thenTheCallIsForwardedWithCorrectParamsOnTheInternalHandler() throws Exception {

        wrapper.uploadFirmware(data, false);

        verify(internalHandlerMock).post(any());
        verify(capabilityMock).uploadFirmware(data, false);
    }

    @Test
    public void whenCallingUploadFirmwareWithResume_thenTheCallIsForwardedWithCorrectParams() throws Exception {

        wrapper.uploadFirmware(data, true);

        verify(internalHandlerMock).post(any());
        verify(capabilityMock).uploadFirmware(data, true);
    }
}