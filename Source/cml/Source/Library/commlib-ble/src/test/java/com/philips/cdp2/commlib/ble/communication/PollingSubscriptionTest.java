/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.communication.PollingSubscription.Callback;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PollingSubscriptionTest {

    @Mock
    private ScheduledExecutorService mockExecutor;

    @Mock
    private
    CommunicationStrategy mockStrategy;

    @Mock
    private
    ResponseHandler mockResponseHandler;

    @Mock
    private
    ScheduledFuture<?> mockFuture;

    @Mock
    private
    CountDownLatch mockCountDownLatch;

    @Mock
    private Callback mockCallback;

    private PortParameters portParameters = new PortParameters("testPort", 42);

    private final List<Long> currentTime = new ArrayList<Long>() {{
        add(0L);
    }};

    private final List<ResponseHandler> capturedHandlers = new ArrayList<>();

    private PollingSubscription pollerUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        when(mockExecutor.scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class))).thenAnswer(new Answer<ScheduledFuture<?>>() {
            @Override
            public ScheduledFuture<?> answer(InvocationOnMock invocation) throws Throwable {
                return mockFuture;
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                capturedHandlers.add((ResponseHandler) invocation.getArgument(2));
                return null;
            }
        }).when(mockStrategy).getProperties(anyString(), anyInt(), any(ResponseHandler.class));

        pollerUnderTest = new PollingSubscription(mockStrategy, mockExecutor, portParameters, 500, 5000, mockResponseHandler) {
            @Override
            long currentTimeMillis() {
                return currentTime.get(0);
            }

            @Override
            CountDownLatch createCountdownLatch() {
                return mockCountDownLatch;
            }
        };
    }

    @Test
    public void whenCreated_thenSubscriptionIsScheduled() {
        verify(mockExecutor).scheduleWithFixedDelay(pollerUnderTest, 0L, 500L, MILLISECONDS);
    }

    @Test
    public void whenRunning_thenCallesGetProperties() {
        pollerUnderTest.run();

        verify(mockStrategy).getProperties(eq("testPort"), eq(42), isA(ResponseHandler.class));
    }

    @Test
    public void whenRunningAfterTTL_thenFutureIsCancelled() {
        currentTime.add(0, 5001L);

        pollerUnderTest.run();

        verify(mockFuture).cancel(anyBoolean());
    }

    @Test
    public void whenRunningAfterTTL_thenCallbackIsCalled() {
        currentTime.add(0, 5001L);
        pollerUnderTest.addCancelCallback(mockCallback);

        pollerUnderTest.run();

        verify(mockCallback).onCancel();
    }

    @Test
    public void whenRunningJustBeforeTTL_thenCallbackIsNotCalled() {
        currentTime.add(0, 5000L);
        pollerUnderTest.addCancelCallback(mockCallback);

        pollerUnderTest.run();

        verify(mockCallback, never()).onCancel();
    }

    @Test
    public void whenHandlerOnSuccessCalled_thenLatchCountdown() throws Exception {
        pollerUnderTest.run();

        capturedHandlers.get(0).onSuccess("");

        verify(mockCountDownLatch).countDown();
    }

    @Test
    public void whenHandlerOnErrorCalled_thenLatchCountdown() throws Exception {
        pollerUnderTest.run();

        capturedHandlers.get(0).onError(null, "");

        verify(mockCountDownLatch).countDown();
    }

    @Test
    public void whenHandlerOnSuccessCalled_thenExternalHandlerCalled() throws Exception {
        pollerUnderTest.run();

        capturedHandlers.get(0).onSuccess("TEST");

        verify(mockResponseHandler).onSuccess("TEST");
    }

    @Test
    public void whenHandlerOnErrorCalled_thenExternalHandlerCalled() throws Exception {
        pollerUnderTest.run();

        capturedHandlers.get(0).onError(Error.IOEXCEPTION, "TEST");

        verify(mockResponseHandler).onError(Error.IOEXCEPTION, "TEST");
    }

    @Test
    public void whenRunning_thenAwaitsCountdown() throws Exception {
        pollerUnderTest.run();

        verify(mockCountDownLatch).await();
    }

    @Test
    public void whenCreatingCountDownLatch_thenCreatesCountDownLatchWithValueOne() throws Exception {
        CountDownLatch countdownLatch = new PollingSubscription(mockStrategy, mockExecutor, portParameters, 500, 5000, mockResponseHandler).createCountdownLatch();

        assertEquals(1, countdownLatch.getCount());
    }
}
