/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware.util;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp.dicommclient.port.DICommPort.SUBSCRIPTION_TTL_S;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;
import static com.philips.cdp2.commlib.core.util.GsonProvider.EMPTY_JSON_OBJECT_STRING;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwarePortStateWaiterTest {

    private static final int PORT_PRODUCT_ID = 10;
    private static final String PORT_NAME = "TawnyPort";

    @Mock
    private FirmwarePort portMock;

    @Mock
    private FirmwarePortProperties portPropertiesMock;

    @Mock
    private FirmwarePortStateWaiter.WaiterListener mockWaiterListener;

    @Mock
    private CountDownLatch mockCountDownLatch;

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    private static final long TIMEOUT_MILLIS = 1000L;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();

        when(portMock.getPortProperties()).thenReturn(portPropertiesMock);
        when(portMock.getDICommPortName()).thenReturn(PORT_NAME);
        when(portMock.getDICommProductId()).thenReturn(PORT_PRODUCT_ID);
        when(portPropertiesMock.getState()).thenReturn(IDLE);
    }

    @Test
    public void whenPortIsInOtherStateThenWaiterShouldReturnNewState() {
        when(portPropertiesMock.getState()).thenReturn(PROGRAMMING);

        FirmwarePortStateWaiter firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, communicationStrategyMock, IDLE, mockWaiterListener);
        firmwarePortStateWaiter.waitForNextState(TIMEOUT_MILLIS);

        ArgumentCaptor<FirmwarePortState> captor = ArgumentCaptor.forClass(FirmwarePortState.class);

        verify(mockWaiterListener, times(1)).onNewState(captor.capture());
        assertFalse(captor.getValue() == IDLE);
    }

    @Test
    public void whenPortIsAlreadyInOtherStateThenSubscribeIsNotCalled() {
        when(portPropertiesMock.getState()).thenReturn(PROGRAMMING);

        FirmwarePortStateWaiter firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, communicationStrategyMock, IDLE, mockWaiterListener);
        firmwarePortStateWaiter.waitForNextState(TIMEOUT_MILLIS);

        verify(communicationStrategyMock, times(0)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCT_ID), eq(SUBSCRIPTION_TTL_S), isA(ResponseHandler.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenPortIsNotInOtherStateThenSubscribeIsCalled() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(portPropertiesMock.getState()).thenReturn(DOWNLOADING);
                invocation.getArgumentAt(0, DICommPortListener.class).onPortUpdate(portMock);
                return null;
            }
        }).when(portMock).addPortListener(any(DICommPortListener.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(mockCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(true);
                return null;
            }
        }).when(mockCountDownLatch).countDown();

        FirmwarePortStateWaiter firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, communicationStrategyMock, IDLE, mockWaiterListener);
        firmwarePortStateWaiter.waitForNextState(TIMEOUT_MILLIS);

        verify(communicationStrategyMock).subscribe(eq(PORT_NAME), eq(PORT_PRODUCT_ID), eq(SUBSCRIPTION_TTL_S), isA(ResponseHandler.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenStateChangedThenReturnsNewState() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(portPropertiesMock.getState()).thenReturn(DOWNLOADING);
                invocation.getArgumentAt(0, SubscriptionEventListener.class).onSubscriptionEventReceived(PORT_NAME, EMPTY_JSON_OBJECT_STRING);
                return null;
            }
        }).when(communicationStrategyMock).addSubscriptionEventListener(any(SubscriptionEventListener.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(mockCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(true);
                return null;
            }
        }).when(mockCountDownLatch).countDown();

        FirmwarePortStateWaiter firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, communicationStrategyMock, IDLE, mockWaiterListener);
        firmwarePortStateWaiter.waitForNextState(TIMEOUT_MILLIS);

        verify(mockWaiterListener, times(1)).onNewState(DOWNLOADING);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenStateCantBeObtainedThenAnErrorIsReported() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, SubscriptionEventListener.class).onSubscriptionEventDecryptionFailed(PORT_NAME);
                return null;
            }
        }).when(communicationStrategyMock).addSubscriptionEventListener(any(SubscriptionEventListener.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(mockCountDownLatch.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(true);
                return null;
            }
        }).when(mockCountDownLatch).countDown();

        FirmwarePortStateWaiter firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, communicationStrategyMock, IDLE, mockWaiterListener);
        firmwarePortStateWaiter.waitForNextState(TIMEOUT_MILLIS);

        verify(mockWaiterListener, never()).onNewState(any(FirmwarePortState.class));
        verify(mockWaiterListener, times(1)).onError(anyString());
    }

    @Test
    public void whenTimeoutThenOnErrorIsCalled() {
        FirmwarePortStateWaiter firmwarePortStateWaiter = new FirmwarePortStateWaiter(portMock, communicationStrategyMock, IDLE, mockWaiterListener) {
            @Override
            void scheduleTask(final TimerTask timeoutTask, long timeoutMillis) {
                timeoutTask.run();
            }
        };
        firmwarePortStateWaiter.waitForNextState(TIMEOUT_MILLIS);

        verify(mockWaiterListener, times(1)).onError(anyString());
    }
}
