/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.discovery;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.util.SsidProvider;
import com.philips.cdp2.commlib.ssdp.SSDPControlPoint;
import com.philips.cdp2.commlib.ssdp.SSDPDevice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static java.util.Collections.unmodifiableCollection;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanDiscoveryStrategyTest extends RobolectricTest {

    @Mock
    private SSDPControlPoint ssdpControlPointMock;

    @Mock
    private DeviceCache deviceCacheMock;

    @Mock
    private ConnectivityMonitor connectivityMonitorMock;

    @Mock
    private SsidProvider ssidProviderMock;

    @Mock
    private DiscoveryListener discoveryListenerMock;

    @Captor
    private ArgumentCaptor<NetworkNode> networkNodeCaptor;

    @Mock
    private Handler mockHandler;

    private LanDiscoveryStrategy strategyUnderTest;

    private AvailabilityListener<ConnectivityMonitor> availabilityListener;

    @Before
    public void setUp() {
        initMocks(this);

        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = (Runnable) invocation.getArguments()[0];
                runnable.run();
                return true;
            }
        }).when(mockHandler).post(any(Runnable.class));

        enableMockedHandler(mockHandler);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                availabilityListener = invocation.getArgument(0);
                return null;
            }
        }).when(connectivityMonitorMock).addAvailabilityListener(ArgumentMatchers.<AvailabilityListener<ConnectivityMonitor>>any());

        strategyUnderTest = new LanDiscoveryStrategy(deviceCacheMock, connectivityMonitorMock, ssidProviderMock) {
            @Override
            SSDPControlPoint createSsdpControlPoint() {
                return ssdpControlPointMock;
            }
        };
        strategyUnderTest.addDiscoveryListener(discoveryListenerMock);
    }

    @Test
    public void whenStartingDiscoveryWithoutArguments_allNetworkNodesShouldBeDiscovered() throws MissingPermissionException {
        strategyUnderTest.start();

        final String cppId = "1234abcd";
        SSDPDevice ssdpDevice = createSsdpDevice(cppId, "CoffeeMaker", "CM9000");
        strategyUnderTest.onDeviceDiscovered(ssdpDevice);

        verify(discoveryListenerMock).onNetworkNodeDiscovered(networkNodeCaptor.capture());
        assertThat(networkNodeCaptor.getValue().getCppId().equals(cppId));
    }

    @Test
    public void givenDiscoveryIsStartedWithModelIds_whenNodesAreDiscovered_thenListenerIsInformedOfNodesWithMatchingModelId() throws MissingPermissionException {
        Set<String> modelIds = Collections.singleton("CM9000");
        strategyUnderTest.start(modelIds);

        SSDPDevice ssdpDevice1 = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        SSDPDevice ssdpDevice2 = createSsdpDevice("tea123", "TeaMaker", "TM8000");

        strategyUnderTest.onDeviceDiscovered(ssdpDevice1);
        strategyUnderTest.onDeviceDiscovered(ssdpDevice2);

        verify(discoveryListenerMock, times(1)).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void givenDiscoveryIsStartedWithModelIds_whenNodesAreDiscovered_thenListenerIsInformedOfNodesWithMatchingModelName() throws MissingPermissionException {
        Set<String> modelIds = new HashSet<>(Arrays.asList("CoffeeMaker"));
        strategyUnderTest.start(modelIds);

        SSDPDevice ssdpDevice1 = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        SSDPDevice ssdpDevice2 = createSsdpDevice("coffee123", "TeaMaker", "UNSUPPORTED");

        strategyUnderTest.onDeviceDiscovered(ssdpDevice1);
        strategyUnderTest.onDeviceDiscovered(ssdpDevice2);

        verify(discoveryListenerMock, times(1)).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void whenADeviceIsDiscoveredTwice_thenListenerIsInformedTwice() throws MissingPermissionException {
        strategyUnderTest.start();

        SSDPDevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");

        strategyUnderTest.onDeviceDiscovered(ssdpDevice);
        when(deviceCacheMock.contains("coffee123")).thenReturn(true);
        strategyUnderTest.onDeviceDiscovered(ssdpDevice);

        verify(discoveryListenerMock, times(2)).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void whenADeviceIsLostViaSSDP_thenTheNetworkNodeShouldBeLost() {
        try {
            strategyUnderTest.start();
        }  catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPDevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");

        strategyUnderTest.onDeviceLost(ssdpDevice);

        verify(discoveryListenerMock).onNetworkNodeLost(networkNodeCaptor.capture());
        assertThat(networkNodeCaptor.getValue().getCppId().equals("coffee123"));
    }

    @Test
    public void whenADeviceIsLostViaTimeout_thenTheNetworkNodeShouldBeLost() {
        final AtomicReference<ExpirationCallback> ref = new AtomicReference<>();

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ref.set((ExpirationCallback) invocation.getArgument(1));
                return null;
            }
        }).when(deviceCacheMock).add(any(NetworkNode.class), any(ExpirationCallback.class), anyLong());

        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPDevice ssdpDevice = createSsdpDevice("coffee123", "CoffeeMaker", "CM9000");
        strategyUnderTest.onDeviceDiscovered(ssdpDevice);
        NetworkNode networkNode = strategyUnderTest.createNetworkNode(ssdpDevice);
        assertNotNull(networkNode);

        ref.get().onCacheExpired(networkNode);

        verify(discoveryListenerMock).onNetworkNodeLost(networkNodeCaptor.capture());
        assertThat(networkNodeCaptor.getValue().getCppId().equals(networkNode.getCppId()));
    }

    @Test
    public void whenAnIncompleteDeviceIsDiscovered_thenTheListenerShouldNotBeInformed() throws MissingPermissionException {
        strategyUnderTest.start();

        SSDPDevice ssdpDevice = createInvalidSsdpDevice("coffee123", "CoffeeMaker", "CM9000");

        strategyUnderTest.onDeviceDiscovered(ssdpDevice);

        verify(discoveryListenerMock, never()).onNetworkNodeDiscovered(any(NetworkNode.class));
    }

    @Test
    public void whenAnIncompleteDeviceIsLost_thenTheNetworkNodeShouldNeverBeLost() {
        try {
            strategyUnderTest.start();
        } catch (MissingPermissionException ignored) {
            fail();
        }

        SSDPDevice ssdpDevice = createInvalidSsdpDevice("coffee123", "CoffeeMaker", "CM9000");

        strategyUnderTest.onDeviceLost(ssdpDevice);

        verify(discoveryListenerMock, never()).onNetworkNodeLost(any(NetworkNode.class));
    }

    @Test
    public void whenCreatingNetworkNodeWithIncompleteSsdpDevice_thenNullShouldBeReturned() {
        NetworkNode networkNode = strategyUnderTest.createNetworkNode(createInvalidSsdpDevice("bla", "bla", "bla"));

        assertThat(networkNode).isNull();
    }

    @Test
    public void whenCreatingInvalidNetworkNode_thenNullShouldBeReturned() {
        SSDPDevice ssdpDevice = createInvalidSsdpDevice("coffee123", "CoffeeMaker", "CM9000");

        NetworkNode networkNode = strategyUnderTest.createNetworkNode(ssdpDevice);

        assertThat(networkNode).isNull();
    }

    @Test
    public void givenDiscoveryIsNotStartedWhenTransportBecomesAvailableThenSsdpIsNotStarted() {
        //setup arranges discovery to not be started

        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        verify(ssdpControlPointMock, never()).start();
    }

    @Test
    public void givenTransportIsNotAvailableWhenDiscoveryIsStartedThenSsdpIsNotStarted() throws MissingPermissionException {
        //setup arranges transport to be unavailable

        strategyUnderTest.start();

        verify(ssdpControlPointMock, never()).start();
    }

    @Test
    public void givenDiscoveryIsStartedWhenTransportBecomesAvailableThenSsdpIsStarted() throws MissingPermissionException {
        strategyUnderTest.start();

        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        verify(ssdpControlPointMock, atLeastOnce()).start();
    }

    @Test
    public void givenTransportIsAvailableWhenDiscoveryIsStartedThenSsdpIsStarted() throws MissingPermissionException {
        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        strategyUnderTest.start();

        verify(ssdpControlPointMock, atLeastOnce()).start();
    }

    @Test
    public void givenTransportIsAvailableAndDiscoveryIsStarted_whenTransportBecomesUnavailable_thenSsdpIsStopped() throws MissingPermissionException {
        ensureConnectivityIsAvailable();
        strategyUnderTest.start();

        when(connectivityMonitorMock.isAvailable()).thenReturn(false);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);

        verify(ssdpControlPointMock, atLeastOnce()).stop();
    }

    @Test
    public void givenTransportIsAvailableAndDiscoveryIsStarted_whenStoppingDiscovery_thenSsdpIsStopped() throws MissingPermissionException {
        ensureConnectivityIsAvailable();
        strategyUnderTest.start();

        strategyUnderTest.stop();

        verify(ssdpControlPointMock, atLeastOnce()).stop();
    }

    @Test
    public void givenTransportIsAvailable_whenStartingDiscovery_thenDiscoveryListenerIsNotified() throws MissingPermissionException {
        ensureConnectivityIsAvailable();

        strategyUnderTest.start();

        verify(discoveryListenerMock).onDiscoveryStarted();
    }

    @Test
    public void givenTransportIsAvailableAndDiscoveryIsStarted_whenStoppingDiscovery_thenDiscoveryListenerIsNotified() throws MissingPermissionException {
        ensureConnectivityIsAvailable();
        strategyUnderTest.start();

        when(ssdpControlPointMock.isDiscovering()).thenReturn(true);
        strategyUnderTest.stop();

        verify(discoveryListenerMock).onDiscoveryStopped();
    }

    @Test
    public void givenDiscoveryCanNotBeStarted_whenDiscoveryIsStarted_thenDiscoveryListenerIsNotified() throws MissingPermissionException {
        doThrow(TransportUnavailableException.class).when(ssdpControlPointMock).start();
        ensureConnectivityIsAvailable();
        strategyUnderTest.start();

        verify(discoveryListenerMock).onDiscoveryFailedToStart();
    }

    @Test
    public void whenClearingDiscoveredNetworkNodes_thenDeviceCacheShouldBeCleared() {
        strategyUnderTest.clearDiscoveredNetworkNodes();

        verify(deviceCacheMock).clear();
    }

    @Test
    public void whenDeviceCacheIsCleared_thenAllNetworkNodesInCacheWillBeLost() {
        CacheData mockCacheData = mock(CacheData.class);
        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockCacheData.getNetworkNode()).thenReturn(mockNetworkNode);
        ArrayList<CacheData> deviceCaches = new ArrayList<>();
        deviceCaches.add(mockCacheData);
        Collection<CacheData> fakeCollection = unmodifiableCollection(deviceCaches);

        when(deviceCacheMock.clear()).thenReturn(fakeCollection);

        strategyUnderTest.clearDiscoveredNetworkNodes();

        verify(discoveryListenerMock).onNetworkNodeLost(mockNetworkNode);
    }

    private SSDPDevice createSsdpDevice(final @NonNull String cppId, final @NonNull String modelName, final @NonNull String modelNumber) {
        final SSDPDevice device = new SSDPDevice();

        device.setCppId(cppId);
        device.setModelName(modelName);
        device.setModelNumber(modelNumber);
        device.setFriendlyName("Coffee Maker 9000");
        device.setIpAddress("1.2.3.4");

        return device;
    }

    private SSDPDevice createInvalidSsdpDevice(final @NonNull String cppId, final @NonNull String modelName, final @NonNull String modelNumber) {
        final SSDPDevice device = createSsdpDevice(cppId, modelName, modelNumber);
        device.setIpAddress("INVALID");

        return device;
    }

    private void ensureConnectivityIsAvailable() throws MissingPermissionException {
        when(connectivityMonitorMock.isAvailable()).thenReturn(true);
        availabilityListener.onAvailabilityChanged(connectivityMonitorMock);
    }
}
