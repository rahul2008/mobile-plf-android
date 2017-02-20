/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import com.philips.cdp.dicommclient.request.ResponseHandler;
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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
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

    private PortParameters portParameters = new PortParameters("testPort", 42);

    private final List<Long> currentTime = new ArrayList<Long>() {{
        add(0L);
    }};

    private PollingSubscription subscription;

    @Before
    public void setUp() {
        initMocks(this);

        when(executor.scheduleWithFixedDelay(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class))).thenAnswer(new Answer<ScheduledFuture<?>>() {
            @Override
            public ScheduledFuture<?> answer(InvocationOnMock invocation) throws Throwable {
                return future;
            }
        });

        subscription = new PollingSubscription(strategy, executor, portParameters, 500, 5000, responseHandler) {
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
    public void testSubscriptionIsScheduledWhenCreated() {
        verify(executor).scheduleWithFixedDelay(subscription, 0L, 500L, MILLISECONDS);
    }

    @Test
    public void testSubscriptionInvokesGetProperties() {
        subscription.run();

        verify(strategy).getProperties(eq("testPort"), eq(42), isA(ResponseHandler.class));
    }

    @Test
    public void testSubscriptionExpires() {
        currentTime.add(0, 5001L);

        subscription.run();

        verify(future).cancel(anyBoolean());
    }
}
