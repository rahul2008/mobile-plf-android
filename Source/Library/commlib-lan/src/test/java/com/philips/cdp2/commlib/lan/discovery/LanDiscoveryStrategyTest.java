/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.discovery;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.discovery.SsdpDiscovery;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.lan.util.WifiNetworkProvider;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanDiscoveryStrategyTest extends RobolectricTest {

    @Mock
    private SsdpDiscovery ssdpDiscoveryMock;

    @Mock
    private LanDeviceCache deviceCacheMock;

    @Mock
    private CacheData cacheDataMock;

    @Mock
    private ConnectivityMonitor connectivityMonitorMock;

    @Mock
    private WifiNetworkProvider wifiNetworkProviderMock;

    @Mock
    private DiscoveryListener discoveryListenerMock;

    @Captor
    ArgumentCaptor<NetworkNode> networkNodeCaptor;

    @Captor
    private ArgumentCaptor<AvailabilityListener<ConnectivityMonitor>> availabilityListenerCaptor;

    private LanDiscoveryStrategy strategyUnderTest;

    private AvailabilityListener<ConnectivityMonitor> availabilityListener;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();

        when(deviceCacheMock.getCacheData(anyString())).thenReturn(cacheDataMock);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                availabilityListener = invocation.getArgumentAt(0, AvailabilityListener.class);
                return null;
            }
        }).when(connectivityMonitorMock).addAvailabilityListener(Matchers.<AvailabilityListener<ConnectivityMonitor>>any());

        strategyUnderTest = new LanDiscoveryStrategy(deviceCacheMock, connectivityMonitorMock, wifiNetworkProviderMock) {
            @Override
            SsdpDiscovery createSsdpDiscovery() {
                return ssdpDiscoveryMock;
            }
        };
        strategyUnderTest.addDiscoveryListener(discoveryListenerMock);
    }

    @Test
    public void whenStartingDiscoveryWithoutArguments_thenOnlyMatchingNetworkNodesShouldBeDiscovered() {
        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }
        final String cppId = "1234abcd";

        SSDPdevice ssdpDevice = createSsdpDevice(cppId, "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);

        strategyUnderTest.onDeviceDiscovered(deviceModel);
        verify(discoveryListenerMock).onNetworkNodeDiscovered(networkNodeCaptor.capture());

        assertThat(networkNodeCaptor.getValue().getCppId().equals(cppId));
    }

    @Test
    public void whenStartingDiscoveryWithDeviceTypes_thenOnlyMatchingNetworkNodesShouldBeDiscovered() {
        Set<String> deviceTypes = Collections.singleton("CoffeeMaker");

        try {
            strategyUnderTest.start(deviceTypes);
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice1 = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel1 = createDeviceModel(ssdpDevice1);

        SSDPdevice ssdpDevice2 = createSsdpDevice("tea123", "TeaMaker", "TM8000");
        DeviceModel deviceModel2 = createDeviceModel(ssdpDevice2);

        strategyUnderTest.onDeviceDiscovered(deviceModel1);
        strategyUnderTest.onDeviceDiscovered(deviceModel2);

        verify(discoveryListenerMock, times(1)).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void whenStartingDiscoveryWithModelIds_thenOnlyMatchingNetworkNodesShouldBeDiscovered() {
        Set<String> deviceTypes = Collections.singleton("CoffeeMaker");
        Set<String> modelIds = Collections.singleton("CM9000");

        try {
            strategyUnderTest.start(deviceTypes, modelIds);
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice1 = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel1 = createDeviceModel(ssdpDevice1);

        SSDPdevice ssdpDevice2 = createSsdpDevice("coffee123", "CoffeeMaker", "UNSUPPORTED");
        DeviceModel deviceModel2 = createDeviceModel(ssdpDevice2);

        strategyUnderTest.onDeviceDiscovered(deviceModel1);
        strategyUnderTest.onDeviceDiscovered(deviceModel2);

        verify(discoveryListenerMock, times(1)).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void whenADeviceIsRediscovered_thenTheNetworkNodeShouldBeUpdated() {
        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);

        strategyUnderTest.onDeviceDiscovered(deviceModel);
        when(deviceCacheMock.contains("coffee123")).thenReturn(true);
        strategyUnderTest.onDeviceDiscovered(deviceModel);

        verify(discoveryListenerMock, times(1)).onNetworkNodeDiscovered(any(NetworkNode.class));
        verify(discoveryListenerMock, times(1)).onNetworkNodeUpdated(any(NetworkNode.class));
    }

    @Test
    public void whenADeviceIsLostViaSSDP_thenTheNetworkNodeShouldBeLost() {
        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);

        strategyUnderTest.onDeviceLost(deviceModel);

        verify(discoveryListenerMock).onNetworkNodeLost(networkNodeCaptor.capture());
        assertThat(networkNodeCaptor.getValue().getCppId().equals("coffee123"));
    }

    @Test
    public void whenADeviceIsLostViaTimeout_thenTheNetworkNodeShouldBeLost() {
        final AtomicReference<ExpirationCallback> ref = new AtomicReference<>();

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ref.set(invocation.getArgumentAt(1, ExpirationCallback.class));
                return null;
            }
        }).when(deviceCacheMock).addNetworkNode(any(NetworkNode.class), any(ExpirationCallback.class), anyLong());

        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);

        strategyUnderTest.onDeviceDiscovered(deviceModel);

        NetworkNode networkNode = strategyUnderTest.createNetworkNode(deviceModel);
        assertNotNull(networkNode);

        ref.get().onCacheExpired(networkNode);

        verify(discoveryListenerMock).onNetworkNodeLost(networkNodeCaptor.capture());
        assertThat(networkNodeCaptor.getValue().getCppId().equals(networkNode.getCppId()));
    }

    @Test
    public void whenAnIncompleteDeviceIsDiscovered_thenTheNetworkNodeShouldNeverBeDiscoveredOrUpdated() {
        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);
        deviceModel.setIpAddress("INVALID");

        strategyUnderTest.onDeviceDiscovered(deviceModel);

        verify(discoveryListenerMock, never()).onNetworkNodeDiscovered(any(NetworkNode.class));
        verify(discoveryListenerMock, never()).onNetworkNodeUpdated(any(NetworkNode.class));
    }

    @Test
    public void whenAnIncompleteDeviceIsLost_thenTheNetworkNodeShouldNeverBeLost() {
        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPdevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);
        deviceModel.setIpAddress("INVALID");

        strategyUnderTest.onDeviceLost(deviceModel);

        verify(discoveryListenerMock, never()).onNetworkNodeLost(any(NetworkNode.class));
    }

    @Test
    public void whenCreatingNetworkNodeWithoutSsdpDevice_thenNullShouldBeReturned() {
        DeviceModel deviceModel = createDeviceModel(null);

        NetworkNode networkNode = strategyUnderTest.createNetworkNode(deviceModel);

        assertThat(networkNode).isNull();
    }

    @Test
    public void whenCreatingInvalidNetworkNode_thenNullShouldBeReturned() {
        SSDPdevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        DeviceModel deviceModel = createDeviceModel(ssdpDevice);
        deviceModel.setIpAddress("INVALID");

        NetworkNode networkNode = strategyUnderTest.createNetworkNode(deviceModel);

        assertThat(networkNode).isNull();
    }

    @Test
    public void givenTransportIsAvailableAndDiscoveryIsStarted_whenStoppingDiscovery_thenSsdpDiscoveryShouldBeStopped() throws MissingPermissionException {
        ensureDiscoveryIsStarted();

        strategyUnderTest.stop();

        verify(ssdpDiscoveryMock).stop();
    }

    @Test
    public void givenDiscoveryIsNotStartedWhenTransportBecomesAvailableThenSsdpIsNotStarted() {
        //setup arranges discovery to not be started

        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        verify(ssdpDiscoveryMock, never()).start();
    }

    @Test
    public void givenTransportIsNotAvailableWhenDiscoveryIsStartedThenSsdpIsNotStarted() {
        //setup arranges transport to be unavailable

        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        verify(ssdpDiscoveryMock, never()).start();
    }

    @Test
    public void givenDiscoveryIsStartedWhenTransportBecomesAvailableThenSsdpIsStarted() throws MissingPermissionException {
        strategyUnderTest.start();

        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        verify(ssdpDiscoveryMock, times(1)).start();
    }

    @Test
    public void givenTransportIsAvailableWhenDiscoveryIsStartedThenSsdpIsStarted() throws MissingPermissionException {
        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        strategyUnderTest.start();

        verify(ssdpDiscoveryMock, times(1)).start();
    }

    @Test
    public void givenTransportIsAvailableAndDiscoveryIsStartedWhenTransportBecomesUnavailableThenSsdpIsStopped() throws MissingPermissionException {
        ensureDiscoveryIsStarted();

        when(connectivityMonitorMock.isAvailable()).thenReturn(false);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        verify(ssdpDiscoveryMock, times(1)).stop();
    }

    private DeviceModel createDeviceModel(final @Nullable SSDPdevice ssdpDevice) {
        final DeviceModel deviceModel = new DeviceModel("nts", "usn", "location", "1.2.3.4", 1337, "bootId");
        deviceModel.setSsdpDevice(ssdpDevice);

        return deviceModel;
    }

    private SSDPdevice createSsdpDevice(final @NonNull String cppId, final @NonNull String deviceType, final @NonNull String modelNumber) {
        final SSDPdevice ssdpDevice = new SSDPdevice();
        ssdpDevice.setCppId(cppId);
        ssdpDevice.setFriendlyName("Coffee Maker 9000");
        ssdpDevice.setModelName(deviceType);
        ssdpDevice.setModelNumber(modelNumber);

        return ssdpDevice;
    }

    private void ensureDiscoveryIsStarted() throws MissingPermissionException {
        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);
        strategyUnderTest.start();
        when(ssdpDiscoveryMock.isStarted()).thenReturn(true);
    }
}
