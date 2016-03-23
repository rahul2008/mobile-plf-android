package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigWearingPosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityConfigWearingPositionWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;

    @Mock
    private SHNCapabilityConfigWearingPosition capabilityMock;

    @Mock
    private SHNSetResultListener<SHNCapabilityConfigWearingPosition.SHNWearingPosition> shnWearingPositionSHNSetResultListener;

    @Mock
    private SHNCapabilityConfigWearingPosition.SHNWearingPositionResultListener shnWearingPositionResultListener;

    @Mock
    private SHNResultListener shnResultListener;

    @Captor
    private ArgumentCaptor<SHNSetResultListener<SHNCapabilityConfigWearingPosition.SHNWearingPosition>> setResultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<SHNCapabilityConfigWearingPosition.SHNWearingPositionResultListener> shnWearingPositionResultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor;

    private SHNCapabilityConfigWearingPositionWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityConfigWearingPositionWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallToGetSupportedWearingPositionOnInternalThread_whenGetConfigWearingPositionIsCalledOnWrapper() {
        capabilityWrapper.getSupportedWearingPositions(shnWearingPositionSHNSetResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getSupportedWearingPositions(setResultListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToGetSupportedWearingPositionOnInternalThread_whenGetConfigWearingPositionIsCalledOnWrapper();

        SHNSetResultListener<SHNCapabilityConfigWearingPosition.SHNWearingPosition> internalResultListener = setResultListenerArgumentCaptor.getValue();

        Set<SHNCapabilityConfigWearingPosition.SHNWearingPosition> shnDataTypes = new HashSet<>();
        internalResultListener.onActionCompleted(shnDataTypes, SHNResult.SHNOk);
        captureUserHandlerRunnable().run();

        verify(shnWearingPositionSHNSetResultListener).onActionCompleted(shnDataTypes, EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallToGetWearingPositionOnInternalThread_whenGetConfigWearingPositionIsCalledOnWrapper() {
        capabilityWrapper.getWearingPosition(shnWearingPositionResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getWearingPosition(shnWearingPositionResultListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenWearingPositionResultReturnOnInternalThread() {
        shouldReceiveCallToGetWearingPositionOnInternalThread_whenGetConfigWearingPositionIsCalledOnWrapper();

        SHNCapabilityConfigWearingPosition.SHNWearingPositionResultListener internalResultListener = shnWearingPositionResultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(SHNCapabilityConfigWearingPosition.SHNWearingPosition.LeftWrist, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(shnWearingPositionResultListener).onActionCompleted(SHNCapabilityConfigWearingPosition.SHNWearingPosition.LeftWrist, EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallToSetWearingPositionOnInternalThread_whenGetConfigWearingPositionIsCalledOnWrapper() {
        capabilityWrapper.setWearingPosition(SHNCapabilityConfigWearingPosition.SHNWearingPosition.Chest, shnResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setWearingPosition(any(SHNCapabilityConfigWearingPosition.SHNWearingPosition.class), shnResultListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenSetWearingPositionResultReturnOnInternalThread() {
        shouldReceiveCallToSetWearingPositionOnInternalThread_whenGetConfigWearingPositionIsCalledOnWrapper();

        SHNResultListener internalResultListener = shnResultListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(shnResultListener).onActionCompleted(EXPECTED_RESULT);
    }
}