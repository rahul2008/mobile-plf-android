/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceCacheTest {
    private DeviceCache<CacheData> deviceCache;

    @Mock
    NetworkNode networkNodeMock;
    @Mock
    NetworkNode secondNetworkNodeMock;

    @Mock
    ScheduledExecutorService scheduledExecutorServiceMock;

    @Mock
    ScheduledFuture scheduledFutureMock;

    @Mock
    DeviceCache.ExpirationCallback expirationCallbackMock;

    @Mock
    CacheData cacheDataMock;
    @Mock
    CacheData secondCacheDataMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        deviceCache = new DeviceCache<>(scheduledExecutorServiceMock);

        when(cacheDataMock.getExpirationPeriodMillis()).thenReturn(5L);
        when(cacheDataMock.getNetworkNode()).thenReturn(networkNodeMock);
        when(cacheDataMock.getExpirationCallback()).thenReturn(expirationCallbackMock);
        when(secondCacheDataMock.getExpirationPeriodMillis()).thenReturn(5L);
        when(secondCacheDataMock.getNetworkNode()).thenReturn(secondNetworkNodeMock);
        when(secondCacheDataMock.getExpirationCallback()).thenReturn(expirationCallbackMock);
        when(networkNodeMock.getCppId()).thenReturn("my-cpp-id");
        when(secondNetworkNodeMock.getCppId()).thenReturn("my-cpp-id-other");
    }

    @Test
    public void whenAddingADevice_ThenCacheShouldContainData() throws Exception {
        deviceCache.add(cacheDataMock);

        assertTrue(deviceCache.contains("my-cpp-id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddingADeviceWith0ExpirationPeriod_ThenThrowsError() {
        when(cacheDataMock.getExpirationPeriodMillis()).thenReturn(0L);

        deviceCache.add(cacheDataMock);
    }

    @Test
    public void whenAddingADeviceTwice_ThenTheTimerShouldReset() throws Exception {
        deviceCache.add(cacheDataMock);
        deviceCache.add(cacheDataMock);

        verify(cacheDataMock).resetTimer();
    }

    @Test
    public void whenStoppingTimers_ThenAllTimersShouldBeStopped() {
        deviceCache.add(cacheDataMock);
        deviceCache.add(secondCacheDataMock);

        deviceCache.stopTimers();

        verify(cacheDataMock).stopTimer();
        verify(secondCacheDataMock).stopTimer();
    }

    @Test
    public void whenResettingTimers_ThenAllTimersShouldBeReset() {
        deviceCache.add(cacheDataMock);
        deviceCache.add(secondCacheDataMock);

        deviceCache.resetTimers();

        verify(cacheDataMock).resetTimer();
        verify(secondCacheDataMock).resetTimer();
    }

    @Test
    public void whenClearingCache_ThenDataIsRemoved() {
        deviceCache.add(cacheDataMock);
        deviceCache.add(secondCacheDataMock);

        deviceCache.clear();

        assertThat(deviceCache.contains(networkNodeMock.getCppId())).isFalse();
        assertThat(deviceCache.contains(secondNetworkNodeMock.getCppId())).isFalse();
    }
}
