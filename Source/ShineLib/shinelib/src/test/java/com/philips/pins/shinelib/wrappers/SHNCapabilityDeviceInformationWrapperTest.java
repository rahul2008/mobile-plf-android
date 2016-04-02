package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityDeviceInformationWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;

    @Mock
    private SHNCapabilityDeviceInformation capabilityMock;

    @Mock
    private SHNCapabilityDeviceInformation.Listener shnResultListener;

    @Captor
    private ArgumentCaptor<SHNCapabilityDeviceInformation.Listener> shnResultListenerArgumentCaptor;

    private SHNCapabilityDeviceInformationWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityDeviceInformationWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallToReadDeviceInformationOnInternalThread_whenReadDeviceInformationIsCalledOnWrapper() {
        capabilityWrapper.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.CTN, shnResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).readDeviceInformation(any(SHNCapabilityDeviceInformation.SHNDeviceInformationType.class), shnResultListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToReadDeviceInformationOnInternalThread_whenReadDeviceInformationIsCalledOnWrapper();

        SHNCapabilityDeviceInformation.Listener internalResultListener = shnResultListenerArgumentCaptor.getValue();

        Date dateWhenAcquired = new Date();
        internalResultListener.onDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.CTN, "CTN", dateWhenAcquired);
        captureUserHandlerRunnable().run();

        verify(shnResultListener).onDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.CTN, "CTN", dateWhenAcquired);
    }

    @Test
    public void shouldReceiveErrorResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToReadDeviceInformationOnInternalThread_whenReadDeviceInformationIsCalledOnWrapper();

        SHNCapabilityDeviceInformation.Listener internalResultListener = shnResultListenerArgumentCaptor.getValue();

        Date dateWhenAcquired = new Date();
        internalResultListener.onError(SHNCapabilityDeviceInformation.SHNDeviceInformationType.CTN, SHNResult.SHNErrorInvalidState);
        captureUserHandlerRunnable().run();

        verify(shnResultListener).onError(SHNCapabilityDeviceInformation.SHNDeviceInformationType.CTN, SHNResult.SHNErrorInvalidState);
    }
}