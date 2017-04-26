/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTest {

    private BleDiscoveryStrategy strategyUnderTest;

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
    BleDeviceCache.CacheData mockCacheData;

    @Before
    public void setUp() {
        initMocks(this);
        enableMockedHandler(new Handler());

        when(mockCentral.getShnDeviceScanner()).thenReturn(mockScanner);

        strategyUnderTest = new BleDiscoveryStrategy(mockContext, mockCache, mockCentral);
    }

    @Test
    public void findDeviceAgain() {
        when(mockDeviceFoundInfo.getDeviceAddress()).thenReturn("ADDR");
        when(mockCache.findByAddress("ADDR")).thenReturn(mockCacheData);
        NetworkNode networkNode = new NetworkNode();
        when(mockCacheData.getNetworkNode()).thenReturn(networkNode);
        strategyUnderTest.addDiscoveryListener(listener);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void findDeviceAgainWithInvalidModelId() {
        strategyUnderTest.modelIds = new HashSet<>();
        strategyUnderTest.modelIds.add("NOT A MODEL");
        when(mockDeviceFoundInfo.getDeviceAddress()).thenReturn("ADDR");
        when(mockCache.findByAddress("ADDR")).thenReturn(mockCacheData);
        NetworkNode networkNode = new NetworkNode();
        when(mockCacheData.getNetworkNode()).thenReturn(networkNode);
        strategyUnderTest.addDiscoveryListener(listener);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener, never()).onNetworkNodeDiscovered(networkNode);
    }
}