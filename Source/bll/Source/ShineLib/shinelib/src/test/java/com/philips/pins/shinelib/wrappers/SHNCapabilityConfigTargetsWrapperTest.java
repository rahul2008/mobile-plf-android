package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigTargets;
import com.philips.pins.shinelib.datatypes.SHNDataType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityConfigTargetsWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;
    public static final double VALUE = 40000D;

    @Mock
    private SHNCapabilityConfigTargets capabilityMock;

    @Mock
    private ResultListener<Set<SHNDataType>> setResultListener;

    @Mock
    private ResultListener<Double> doubleResultListener;

    @Captor
    private ArgumentCaptor<ResultListener<Set<SHNDataType>>> setResultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResultListener<Double>> doubleListenerArgumentCaptor;


    private SHNCapabilityConfigTargetsWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityConfigTargetsWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallToGetConfigTargetsLevelOnInternalThread_whenGetConfigTargetsIsCalledOnWrapper() {
        capabilityWrapper.getSupportedDataTypes(setResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getSupportedDataTypes(setResultListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToGetConfigTargetsLevelOnInternalThread_whenGetConfigTargetsIsCalledOnWrapper();

        ResultListener<Set<SHNDataType>> internalResultListener = setResultListenerArgumentCaptor.getValue();

        Set<SHNDataType> shnDataTypes = new HashSet<>();
        internalResultListener.onActionCompleted(shnDataTypes, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(setResultListener).onActionCompleted(shnDataTypes, EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallToGetTargetForTypOnInternalThread_whenGetConfigTargetsIsCalledOnWrapper() {
        capabilityWrapper.getTargetForType(SHNDataType.ActivityTypeMoonshine, doubleResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).getTargetForType(any(SHNDataType.class), doubleListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultForTargetsOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToGetTargetForTypOnInternalThread_whenGetConfigTargetsIsCalledOnWrapper();

        ResultListener<Double> internalResultListener = doubleListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(doubleResultListener).onActionCompleted(VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldReceiveCallToSetTargetForTypOnInternalThread_whenGetConfigTargetsIsCalledOnWrapper() {
        capabilityWrapper.setTarget(SHNDataType.ActivityTypeMoonshine, VALUE, doubleResultListener);

        captureInternalHandlerRunnable().run();

        verify(capabilityMock).setTarget(any(SHNDataType.class), anyDouble(), doubleListenerArgumentCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultForSetTargetsOnUserThread_whenResultReturnOnInternalThread() {
        shouldReceiveCallToSetTargetForTypOnInternalThread_whenGetConfigTargetsIsCalledOnWrapper();

        ResultListener<Double> internalResultListener = doubleListenerArgumentCaptor.getValue();

        internalResultListener.onActionCompleted(VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(doubleResultListener).onActionCompleted(VALUE, EXPECTED_RESULT);
    }
}