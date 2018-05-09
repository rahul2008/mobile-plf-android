/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CacheDataTest {

    @Mock
    private ScheduledExecutorService executorMock;

    @Mock
    private DeviceCache.ExpirationCallback expirationCallbackMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private ScheduledFuture futureMock;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void whenConstructorCalled_ThenCacheDataIsCreated() {
        final CacheData cacheData = new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        assertThat(cacheData).isNotNull();
    }

    @Test
    public void whenConstructorCalled_ThenExpirationTimeoutIsStarted() {
        new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        verify(executorMock).schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class));
    }

    @Test
    public void whenTimeExpires_ThenExpirationCallbackIsCalled() throws Exception {
        final Callable[] callables = new Callable[1];
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callables[0] = invocation.getArgument(0);
                return futureMock;
            }
        }).when(executorMock).schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class));
        new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        callables[0].call();

        verify(expirationCallbackMock).onCacheExpired(networkNodeMock);
    }

    @Test
    public void whenResetCalled_ThenCurrentFutureCancelled() {
        doAnswer(new Answer() {
            @Override
            public ScheduledFuture answer(InvocationOnMock invocation) throws Throwable {
                return futureMock;
            }
        }).when(executorMock).schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class));
        final CacheData cacheData = new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        cacheData.resetTimer();

        verify(futureMock).cancel(true);
    }

    @Test
    public void whenResetCalled_ThenScheduledAgain() {
        final CacheData cacheData = new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        cacheData.resetTimer();

        verify(executorMock, times(2)).schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class));
    }

    @Test
    public void whenStopCalled_ThenCurrentFutureCancelled() {
        doAnswer(new Answer() {
            @Override
            public ScheduledFuture answer(InvocationOnMock invocation) throws Throwable {
                return futureMock;
            }
        }).when(executorMock).schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class));
        final CacheData cacheData = new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        cacheData.stopTimer();

        verify(futureMock).cancel(true);
    }

    @Test
    public void givenStopIsCalled_whenDataExpires_thenExpirationCallbackIsNotCalled() throws Exception {
        final Callable[] callables = new Callable[1];
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                callables[0] = invocation.getArgument(0);
                return null;
            }
        }).when(executorMock).schedule(isA(Callable.class), anyLong(), isA(TimeUnit.class));
        final CacheData cacheData = new CacheData(executorMock, expirationCallbackMock, 0L, networkNodeMock);

        cacheData.stopTimer();

        //this simulates that an already running future cannot be cancelled (it's running and there's nothing you can do about it)
        callables[0].call();

        verify(expirationCallbackMock, times(0)).onCacheExpired(networkNodeMock);
    }
}
