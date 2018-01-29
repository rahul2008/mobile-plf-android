/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.DeviceCacheListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceCacheTest {

    private DeviceCache<CacheData> deviceCache;

    @Mock
    ScheduledExecutorService scheduledExecutorServiceMock;

    @Mock
    NetworkNode networkNodeMock;

    @Mock
    NetworkNode secondNetworkNodeMock;

    @Mock
    CacheData cacheDataMock;

    @Mock
    CacheData secondCacheDataMock;

    @Mock
    DeviceCacheListener<CacheData> listener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        deviceCache = new DeviceCache<>(scheduledExecutorServiceMock);

        when(networkNodeMock.getCppId()).thenReturn("my-cpp-id");
        when(secondNetworkNodeMock.getCppId()).thenReturn("my-cpp-id-other");

        when(cacheDataMock.getExpirationPeriodMillis()).thenReturn(5L);
        when(cacheDataMock.getNetworkNode()).thenReturn(networkNodeMock);

        when(secondCacheDataMock.getExpirationPeriodMillis()).thenReturn(5L);
        when(secondCacheDataMock.getNetworkNode()).thenReturn(secondNetworkNodeMock);
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
    public void whenClearingCache_ThenDataIsRemoved_AndListenersNotified() {
        deviceCache.add(cacheDataMock);
        deviceCache.add(secondCacheDataMock);

        deviceCache.addDeviceCacheListener(listener);

        deviceCache.clear();

        assertThat(deviceCache.contains(networkNodeMock.getCppId())).isFalse();
        assertThat(deviceCache.contains(secondNetworkNodeMock.getCppId())).isFalse();

        verify(listener).onRemoved(cacheDataMock);
    }

    @Test
    public void whenClearingCache_ThenCacheIsEmpty_AndDataIsReturned() {
        deviceCache.add(cacheDataMock);
        deviceCache.add(secondCacheDataMock);

        Collection<CacheData> clearedData = deviceCache.clear();

        assertThat(deviceCache.contains(networkNodeMock.getCppId())).isFalse();
        assertThat(deviceCache.contains(secondNetworkNodeMock.getCppId())).isFalse();

        assertThat(clearedData.size()).isEqualTo(2);
    }

    @Test
    public void whenDeviceAdded_ThenListenerIsNotified() {
        deviceCache.addDeviceCacheListener(listener);

        deviceCache.add(cacheDataMock);

        verify(listener).onAdded(cacheDataMock);
    }

    @Test
    public void whenDeviceRemoved_ThenListenerIsNotified() {
        deviceCache.addDeviceCacheListener(listener);

        deviceCache.add(cacheDataMock);
        deviceCache.remove(networkNodeMock.getCppId());

        verify(listener).onRemoved(cacheDataMock);
    }
}
