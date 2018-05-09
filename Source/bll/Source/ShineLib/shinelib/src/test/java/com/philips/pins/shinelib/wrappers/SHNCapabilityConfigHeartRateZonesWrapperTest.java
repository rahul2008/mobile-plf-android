package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigHeartRateZones;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityConfigHeartRateZonesWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final Integer EXPECTED_THRESHOLDS_COUNT = 6;
    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNErrorLogSyncBufferFormat;

    @Mock
    private SHNCapabilityConfigHeartRateZones capabilityMock;

    @Mock
    private ResultListener<Integer> mockedIntegerResultListener;

    @Mock
    private ResultListener<List<Integer>> mockedIntegerListResultListener;

    @Mock
    private SHNResultListener mockedSHNResultListener;

    @Mock
    private List<Integer> mockedThresholdsList;

    private SHNCapabilityConfigHeartRateZonesWrapper capabilityWrapper;


    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityConfigHeartRateZonesWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    private void callGetThresholdsCountOnInternalHandler() {
        capabilityWrapper.getSupportedHeartRateZoneThresholdsCount(mockedIntegerResultListener);
        verify(capabilityMock, never()).getSupportedHeartRateZoneThresholdsCount(any(ResultListener.class));

        captureInternalHandlerRunnable().run();
    }

    private void callGetHeartRateZonesOnInternalHandler() {
        capabilityWrapper.getHeartRateZoneThresholdsInBpm(mockedIntegerListResultListener);
        verify(capabilityMock, never()).getHeartRateZoneThresholdsInBpm(any(ResultListener.class));

        captureInternalHandlerRunnable().run();
    }

    private void callSetHeartRateZoneThresholdsOnInternalHandler() {
        capabilityWrapper.setHeartRateZoneThresholdsInBpm(mockedThresholdsList, mockedSHNResultListener);
        verify(capabilityMock, never()).setHeartRateZoneThresholdsInBpm(ArgumentMatchers.<Integer>anyList(), any(SHNResultListener.class));

        captureInternalHandlerRunnable().run();
    }

    private ResultListener getInternalResultListenerForGetThresholdsCountCall() {
        ArgumentCaptor<ResultListener> internalResultListenerCaptor = ArgumentCaptor.forClass(ResultListener.class);
        verify(capabilityMock).getSupportedHeartRateZoneThresholdsCount(internalResultListenerCaptor.capture());

        return internalResultListenerCaptor.getValue();
    }

    private ResultListener getInternalResultListenerForGetThresholdsCall() {
        ArgumentCaptor<ResultListener> internalResultListenerCaptor = ArgumentCaptor.forClass(ResultListener.class);
        verify(capabilityMock).getHeartRateZoneThresholdsInBpm(internalResultListenerCaptor.capture());

        return internalResultListenerCaptor.getValue();
    }

    private SHNResultListener getInternalResultListenerForSetThresholdsCall() {
        ArgumentCaptor<SHNResultListener> internalResultListenerCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        verify(capabilityMock).setHeartRateZoneThresholdsInBpm(eq(mockedThresholdsList), internalResultListenerCaptor.capture());

        return internalResultListenerCaptor.getValue();
    }

    @Test
    public void whenGetSupportedHeartRateZoneThresholdsCountIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler() {
        callGetThresholdsCountOnInternalHandler();

        verify(capabilityMock).getSupportedHeartRateZoneThresholdsCount(any(ResultListener.class));
    }

    @Test
    public void whenGetHeartRateZoneThresholdsInBpmIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler() {
        callGetHeartRateZonesOnInternalHandler();

        verify(capabilityMock).getHeartRateZoneThresholdsInBpm(any(ResultListener.class));
    }

    @Test
    public void whenSetHeartRateZoneThresholdsInBpmIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler() {
        callSetHeartRateZoneThresholdsOnInternalHandler();

        verify(capabilityMock).setHeartRateZoneThresholdsInBpm(eq(mockedThresholdsList), any(SHNResultListener.class));
    }

    @Test
    public void whenGetSupportedHeartRateZoneThresholdsCountIsCalled__ItPostsResultsOnTheUserHandler() {
        callGetThresholdsCountOnInternalHandler();

        getInternalResultListenerForGetThresholdsCountCall().onActionCompleted(EXPECTED_THRESHOLDS_COUNT, EXPECTED_RESULT);
        verify(mockedIntegerResultListener, never()).onActionCompleted(anyInt(), any(SHNResult.class));

        captureUserHandlerRunnable().run();

        verify(mockedIntegerResultListener).onActionCompleted(EXPECTED_THRESHOLDS_COUNT, EXPECTED_RESULT);
    }

    @Test
    public void whenGetHeartRateZoneThresholdsInBpmIsCalled_ItPostsResultsOnTheUserHandler() {
        callGetHeartRateZonesOnInternalHandler();

        getInternalResultListenerForGetThresholdsCall().onActionCompleted(mockedThresholdsList, EXPECTED_RESULT);
        verify(mockedIntegerListResultListener, never()).onActionCompleted(ArgumentMatchers.<Integer>anyList(), any(SHNResult.class));

        captureUserHandlerRunnable().run();

        verify(mockedIntegerListResultListener).onActionCompleted(mockedThresholdsList, EXPECTED_RESULT);
    }

    @Test
    public void whenSetHeartRateZoneThresholdsInBpmIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler_AndPostsResultsOnTheUserHandler() {
        callSetHeartRateZoneThresholdsOnInternalHandler();

        getInternalResultListenerForSetThresholdsCall().onActionCompleted(EXPECTED_RESULT);
        verify(mockedSHNResultListener, never()).onActionCompleted(any(SHNResult.class));

        captureUserHandlerRunnable().run();

        verify(mockedSHNResultListener).onActionCompleted(EXPECTED_RESULT);
    }
}