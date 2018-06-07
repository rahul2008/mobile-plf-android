/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.utility.BleScanRecord;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.HashSet;

import static com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy.MANUFACTURER_PREAMBLE;
import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTest {

    private static final String MAC_ADDRESS = "ADDR";
    private static final String MODEL_ID = "NK1234";

    private BleDiscoveryStrategy strategyUnderTest;

    @Mock
    private
    Handler mockHandler;

    @Mock
    private Context mockContext;

    @Mock
    private BleDeviceCache mockCache;

    @Mock
    private SHNDeviceScanner mockScanner;

    @Mock
    private SHNCentral mockCentral;

    @Mock
    private DiscoveryListener listener;

    @Mock
    private SHNDeviceFoundInfo mockDeviceFoundInfo;

    @Mock
    private BleScanRecord mockBleScanRecord;

    @Mock
    private SHNDevice mockDevice;

    @Mock
    private BleCacheData mockCacheData;

    private NetworkNode networkNode;

    @Before
    public void setUp() {
        initMocks(this);
        enableMockedHandler(mockHandler);

        networkNode = new NetworkNode();
        networkNode.setCppId(MAC_ADDRESS);

        when(mockCentral.getShnDeviceScanner()).thenReturn(mockScanner);
        when(mockDeviceFoundInfo.getShnDevice()).thenReturn(mockDevice);
        when(mockDevice.getAddress()).thenReturn(MAC_ADDRESS);
        when(mockDeviceFoundInfo.getBleScanRecord()).thenReturn(mockBleScanRecord);
        when(mockBleScanRecord.getManufacturerSpecificData(MANUFACTURER_PREAMBLE)).thenReturn(MODEL_ID.getBytes());
        when(mockCache.getCacheData(MAC_ADDRESS)).thenReturn(mockCacheData);
        when(mockCacheData.getNetworkNode()).thenReturn(networkNode);

        strategyUnderTest = new BleDiscoveryStrategy(mockContext, mockCache, mockScanner);
        strategyUnderTest.addDiscoveryListener(listener);
    }

    @Test
    public void whenADeviceIsFound_thenTheNetworkNodeIsFilledWithTheProperValues() throws Exception {

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        ArgumentCaptor<NetworkNode> captor = ArgumentCaptor.forClass(NetworkNode.class);
        verify(listener).onNetworkNodeDiscovered(captor.capture());
        NetworkNode capturedNode = captor.getValue();
        assertThat(capturedNode.getMacAddress()).isEqualTo(MAC_ADDRESS);
        assertThat(capturedNode.getCppId()).isEqualTo(MAC_ADDRESS);
        assertThat(capturedNode.getModelId()).isEqualTo(MODEL_ID);
    }

    @Test
    public void whenADeviceIsFoundWhichModelIdDoesntMatchTheFilterNoNetworkNodesShouldBeDiscovered() {
        strategyUnderTest.modelIds = new HashSet<>();
        strategyUnderTest.modelIds.add("NOT A MODEL");

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener, never()).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void whenDeviceDiscoveredTwice_ThenItIsReportedTwice() {
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);
        when(mockCache.contains(MAC_ADDRESS)).thenReturn(true);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(listener, times(2)).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenDeviceDiscoveredTwice_ThenItsCacheTimerMustBeReset_AndItsAvailabilitySetToTrue() {
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);
        when(mockCache.contains(MAC_ADDRESS)).thenReturn(true);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(mockCacheData).resetTimer();
        verify(mockCacheData).setAvailable(true);
    }

    @Test
    public void givenADeviceIsDiscovered_whenClearDiscoveredNetworkNodesIsInvoked_thenCacheShouldBeCleared() {
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        strategyUnderTest.clearDiscoveredNetworkNodes();

        verify(mockCache).clear();
    }
}
