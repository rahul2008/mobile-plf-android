/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.communication.PollingSubscription.Callback;
import com.philips.commlib.core.communication.CommunicationStrategy;

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

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PollingSubscriptionTest {

    @Mock
    ScheduledExecutorService executor;

    @Mock
    CommunicationStrategy strategy;

    @Mock
    ResponseHandler responseHandler;

    @Mock
    ScheduledFuture<?> future;

    @Mock
    CountDownLatch countDownLatch;

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

        when(executor.scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class))).thenAnswer(new Answer<ScheduledFuture<?>>() {
            @Override
            public ScheduledFuture<?> answer(InvocationOnMock invocation) throws Throwable {
                return future;
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                capturedHandlers.add(invocation.getArgumentAt(2, ResponseHandler.class));
                return null;
            }
        }).when(strategy).getProperties(anyString(), anyInt(), any(ResponseHandler.class));

        pollerUnderTest = new PollingSubscription(strategy, executor, portParameters, 500, 5000, responseHandler) {
            @Override
            long currentTimeMillis() {
                return currentTime.get(0);
            }

            @Override
            CountDownLatch createCountdownLatch() {
                return countDownLatch;
            }
        };
    }

    @Test
    public void whenCreated_thenSubscriptionIsScheduled() {
        verify(executor).scheduleWithFixedDelay(pollerUnderTest, 0L, 500L, MILLISECONDS);
    }

    @Test
    public void whenRunning_thenCallesGetProperties() {
        pollerUnderTest.run();

        verify(strategy).getProperties(eq("testPort"), eq(42), isA(ResponseHandler.class));
    }

    @Test
    public void whenRunningAfterTTL_thenFutureIsCancelled() {
        currentTime.add(0, 5001L);

        pollerUnderTest.run();

        verify(future).cancel(anyBoolean());
    }

    @Test
    public void whenRunningAfterTTL_thenCallbackIsCalled() {
        currentTime.add(0, 5001L);
        pollerUnderTest.addCancelCallback(mockCallback);

        pollerUnderTest.run();

        verify(mockCallback).onCancel();
    }
}
