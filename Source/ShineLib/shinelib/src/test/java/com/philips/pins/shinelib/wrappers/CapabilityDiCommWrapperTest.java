/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

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
    public static final SHNDataRaw READ_VALUE = new SHNDataRaw(READ_RAW);

    @Mock
    private CapabilityDiComm capabilityMock;

    @Mock
    private ResultListener<SHNDataRaw> rawResultListener;

    @Mock
    private ResultListener<SHNDataRaw> rawResultListener2;

    @Mock
    private SHNResultListener resultListener;

    @Captor
    private ArgumentCaptor<SHNResultListener> resultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResultListener<SHNDataRaw>> rawResultListenerCaptor;

    private CapabilityDiCommWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);
        capabilityWrapper = new CapabilityDiCommWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    @Test
    public void shouldReceiveCallWriteDataOnInternalThread_whenWriteDataIsCalledOnWrapper() {
        capabilityWrapper.writeData(READ_RAW);
        captureInternalHandlerRunnable().run();

        verify(capabilityMock).writeData(eq(READ_RAW));
    }

    @Test
    public void shouldAddListener_whenAddListenerIsCalledOnWrapper() {
        capabilityWrapper.addDataListener(rawResultListener);

        verify(capabilityMock).addDataListener(rawResultListenerCaptor.capture());
    }

    @Test
    public void shouldReceiveCorrectResultOnUserThread_whenDataResultReturnOnInternalThread() {
        shouldAddListener_whenAddListenerIsCalledOnWrapper();
        ResultListener<SHNDataRaw> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldNotifyAllListenersAllRegisteredListener_whenDataResultReturnOnInternalThread() {
        capabilityWrapper.addDataListener(rawResultListener);
        capabilityWrapper.addDataListener(rawResultListener2);

        verify(capabilityMock).addDataListener(rawResultListenerCaptor.capture());

        ResultListener<SHNDataRaw> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        verify(rawResultListener2).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }

    @Test
    public void shouldNotNotifyListener_whenTheListenerIsRemoved() {
        shouldAddListener_whenAddListenerIsCalledOnWrapper();
        capabilityWrapper.removeDataListener(rawResultListener);
        ResultListener<SHNDataRaw> internalResultListener = rawResultListenerCaptor.getValue();

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

        ResultListener<SHNDataRaw> internalResultListener = rawResultListenerCaptor.getValue();

        internalResultListener.onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        captureUserHandlerRunnable().run();

        verify(rawResultListener, never()).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
        verify(rawResultListener2).onActionCompleted(READ_VALUE, EXPECTED_RESULT);
    }
}