/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.utility.BleScanRecord;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTest {

    private BleDiscoveryStrategy strategyUnderTest;

    @Mock
    Handler mockHandler;
    @Mock
    Context mockContext;
    @Mock
    BleDeviceCache mockCache;
    @Mock
    SHNDeviceScanner mockScanner;
    @Mock
    SHNCentral mockCentral;
    @Mock
    DiscoveryStrategy.DiscoveryListener listener;
    @Mock
    SHNDeviceFoundInfo mockDeviceFoundInfo;
    @Mock
    private BleScanRecord mockBleScanRecord;
    @Mock
    private SHNDevice mockDevice;
    @Mock
    BleCacheData mockCacheData;

    private NetworkNode networkNode;

    @Before
    public void setUp() {
        initMocks(this);
        enableMockedHandler(mockHandler);

        networkNode = new NetworkNode();
        networkNode.setCppId("ADDR");

        when(mockCentral.getShnDeviceScanner()).thenReturn(mockScanner);

        when(mockDeviceFoundInfo.getShnDevice()).thenReturn(mockDevice);
        when(mockDevice.getAddress()).thenReturn("ADDR");

        when(mockDeviceFoundInfo.getBleScanRecord()).thenReturn(mockBleScanRecord);

        when(mockCache.getCacheData("ADDR")).thenReturn(mockCacheData);
        when(mockCacheData.getNetworkNode()).thenReturn(networkNode);

        strategyUnderTest = new BleDiscoveryStrategy(mockContext, mockCache, mockScanner);
    }

    @Test
    public void whenADeviceIsFoundANetworkNodeShouldBeDiscovered() {
        strategyUnderTest.addDiscoveryListener(listener);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenADeviceIsFoundWhichModelIdDoesntMatchTheFilterNoNetworkNodesShouldBeDiscovered() {
        strategyUnderTest.modelIds = new HashSet<>();
        strategyUnderTest.modelIds.add("NOT A MODEL");

        strategyUnderTest.addDiscoveryListener(listener);
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener, never()).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenDeviceDiscoveredTwice_ThenItIsReportedTwice() {
        strategyUnderTest.addDiscoveryListener(listener);
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);
        when(mockCache.contains("ADDR")).thenReturn(true);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener, times(2)).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenDeviceDiscoveredTwice_ThenItsCacheTimerMustBeReset_AndItsAvailabilitySetToTrue() {
        strategyUnderTest.addDiscoveryListener(listener);
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);
        when(mockCache.contains("ADDR")).thenReturn(true);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(mockCacheData).resetTimer();
        verify(mockCacheData).setAvailable(true);
    }
}
