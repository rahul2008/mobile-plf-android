/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;

import com.philips.pins.shinelib.datatypes.StreamData;
import com.philips.pins.shinelib.capabilities.StreamIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CapabilityDiCommWrapperTest extends SHNCapabilityWrapperTestBase {

    public static final SHNResult EXPECTED_RESULT = SHNResult.SHNOk;
    public static final byte[] READ_RAW = new byte[]{0x42};
    public static final StreamData READ_VALUE = new StreamData(READ_RAW, StreamIdentifier.STREAM_1);

    @Mock
    private CapabilityDiComm capabilityMock;

    @Mock
    private ResultListener<StreamData> rawResultListener;

    @Mock
    private ResultListener<StreamData> rawResultListener2;

    @Mock
    private SHNResultListener resultListener;

    @Captor
    private ArgumentCaptor<SHNResultListener> resultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResultListener<StreamData>> rawResultListenerCaptor;

    private CapabilityDiCommWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);
        capabilityWrapper = new CapabilityDiCommWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallWriteDataOnInternalThread_whenWriteDataIsCalledOnWrapper() {
        capabilityWrapper.writeData(READ_RAW, StreamIdentifier.STREAM_1);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).writeData(eq(READ_RAW), eq(StreamIdentifier.STREAM_1));
    }

    @Test
    public void shouldAddListener_whenAddListenerIsCalledOnWrapper() {
        capabilityWrapper.addDataListener(rawResultListener);

        verify(capabilityMock).addDataListener(rawResultListenerCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenDataResultReturnOnInternalThread() {
        shouldAddListener_whenAddListenerIsCalledOnWrapper();
        ResultListener<StreamData> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldNotifyAllListenersAllRegisteredListener_whenDataResultReturnOnInternalThread() {
        capabilityWrapper.addDataListener(rawResultListener);
        capabilityWrapper.addDataListener(rawResultListener2);

        verify(capabilityMock).addDataListener(rawResultListenerCaptor.capture());

        ResultListener<StreamData> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        verify(rawResultListener2).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldNotNotifyListener_whenTheListenerIsRemoved() {
        shouldAddListener_whenAddListenerIsCalledOnWrapper();
        capabilityWrapper.removeDataListener(rawResultListener);
        ResultListener<StreamData> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener, never()).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldNotifyRemainingListenersOnUserThread_whenDataResultReturnOnInternalThread() {
        capabilityWrapper.addDataListener(rawResultListener);
        capabilityWrapper.addDataListener(rawResultListener2);

        verify(capabilityMock).addDataListener(rawResultListenerCaptor.capture());
        capabilityWrapper.removeDataListener(rawResultListener);

        ResultListener<StreamData> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener, never()).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        verify(rawResultListener2).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }
}