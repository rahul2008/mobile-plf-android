package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityClearUserData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityClearUserDataWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNErrorLogSyncBufferFormat;

    @Mock
    private SHNCapabilityClearUserData capabilityMock;

    @Mock
    private SHNResultListener resultListenerMock;

    @Captor
    ArgumentCaptor<SHNResultListener> internalResultListenerCaptor;

    private SHNCapabilityClearUserDataWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityClearUserDataWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallToClearUserDataOnInternalThread_whenClearUserDataIsCalledOnWrapper() {
        capabilityWrapper.clearUserData(resultListenerMock);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).clearUserData(internalResultListenerCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToClearUserDataOnInternalThread_whenClearUserDataIsCalledOnWrapper();

        SHNResultListener internalResultListener = internalResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(resultListenerMock).onActionCompleted(EXPECTED_RESULT);
    }
}