/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.utility.BleScanRecord;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy.MANUFACTURER_PREAMBLE;
import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed;
import static java.util.Collections.unmodifiableCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContextCompat.class})
public class BleDiscoveryStrategyTest {

    private static final String MAC_ADDRESS = "ADDR";
    private static final String MODEL_ID = "NK1234";

    private static final String CPP_ID = "ADDR";
    private static final long SCAN_WINDOW_MILLIS = 60000L;

    private BleDiscoveryStrategy strategyUnderTest;

    @Mock
    private Handler mockHandler;

    @Mock
    private Context mockContext;

    @Mock
    private DeviceCache mockCache;

    @Mock
    private SHNDeviceScanner mockScanner;

    @Mock
    private SHNCentral mockCentral;

    @Mock
    private SHNDeviceFoundInfo mockDeviceFoundInfo;

    @Mock
    private BleScanRecord mockBleScanRecord;

    @Mock
    private SHNDevice mockDevice;

    @Mock
    private CacheData mockCacheData;

    @Mock
    private DiscoveryStrategy.DiscoveryListener mockDiscoveryListener;

    @Mock
    private ScheduledExecutorService mockExecutor;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private NetworkNode networkNode;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(ContextCompat.class);
        DICommLog.disableLogging();

        enableMockedHandler(mockHandler);

        networkNode = new NetworkNode();
        networkNode.setCppId(MAC_ADDRESS);
        networkNode.setCppId(CPP_ID);

        when(mockCentral.getShnDeviceScanner()).thenReturn(mockScanner);
        when(mockDeviceFoundInfo.getShnDevice()).thenReturn(mockDevice);
        when(mockDevice.getAddress()).thenReturn(MAC_ADDRESS);
        when(mockDeviceFoundInfo.getBleScanRecord()).thenReturn(mockBleScanRecord);
        when(mockBleScanRecord.getManufacturerSpecificData(MANUFACTURER_PREAMBLE)).thenReturn(MODEL_ID.getBytes());
        when(mockCache.getCacheData(MAC_ADDRESS)).thenReturn(mockCacheData);
        when(mockCacheData.getNetworkNode()).thenReturn(networkNode);

        PowerMockito.when(ContextCompat.checkSelfPermission(mockContext, ACCESS_COARSE_LOCATION)).thenReturn(PERMISSION_GRANTED);

        strategyUnderTest = new BleDiscoveryStrategy(mockContext, mockCache, mockScanner){
            @NonNull
            @Override
            ScheduledExecutorService createExecutor() {
                return mockExecutor;
            }
        };
        strategyUnderTest.addDiscoveryListener(mockDiscoveryListener);
    }

    @Test
    public void whenADeviceIsFound_thenTheNetworkNodeIsFilledWithTheProperValues() throws Exception {

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        ArgumentCaptor<NetworkNode> captor = ArgumentCaptor.forClass(NetworkNode.class);
        verify(mockDiscoveryListener).onNetworkNodeDiscovered(captor.capture());
        NetworkNode capturedNode = captor.getValue();
        assertThat(capturedNode.getMacAddress()).isEqualTo(MAC_ADDRESS);
        assertThat(capturedNode.getCppId()).isEqualTo(MAC_ADDRESS);
        assertThat(capturedNode.getModelId()).isEqualTo(MODEL_ID);
    }

    @Test
    public void whenADeviceIsFoundANetworkNodeShouldBeDiscovered() {

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(mockDiscoveryListener).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenADeviceIsFoundWhichModelIdDoesntMatchTheFilterNoNetworkNodesShouldBeDiscovered() {
        strategyUnderTest.modelIds = new HashSet<>();
        strategyUnderTest.modelIds.add("NOT A MODEL");

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(mockDiscoveryListener, never()).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenDeviceDiscoveredTwice_ThenItIsReportedTwice() {
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);
        when(mockCache.contains(MAC_ADDRESS)).thenReturn(true);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(mockDiscoveryListener, times(2)).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenDeviceDiscoveredTwice_ThenItsCacheTimerMustBeReset_AndItsAvailabilitySetToTrue() {
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);
        when(mockCache.contains(MAC_ADDRESS)).thenReturn(true);

        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        verify(mockCacheData).resetTimer();
    }

    @Test
    public void givenADeviceIsDiscovered_whenClearDiscoveredNetworkNodesIsInvoked_thenCacheShouldBeCleared() {
        strategyUnderTest.deviceFound(mockScanner, mockDeviceFoundInfo);

        strategyUnderTest.clearDiscoveredNetworkNodes();

        verify(mockCache).clear();
    }

    @Test
    public void givenScanCanBeStarted_whenDiscoveryIsStarted_thenListenerIsNotified() throws MissingPermissionException {
        when(mockScanner.startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), eq(DuplicatesAllowed), eq(SCAN_WINDOW_MILLIS))).thenReturn(true);

        strategyUnderTest.start();
        verify(mockExecutor).scheduleAtFixedRate(runnableCaptor.capture(), eq(0L), eq(30L), eq(TimeUnit.SECONDS));
        runnableCaptor.getValue().run();

        verify(mockDiscoveryListener).onDiscoveryStarted();
    }

    @Test
    public void givenScanCannotBeStarted_whenDiscoveryIsStarted_thenListenerIsNotified() throws MissingPermissionException {
        when(mockScanner.startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), eq(DuplicatesAllowed), eq(SCAN_WINDOW_MILLIS))).thenReturn(false);

        strategyUnderTest.start();
        verify(mockExecutor).scheduleAtFixedRate(runnableCaptor.capture(), eq(0L), eq(30L), eq(TimeUnit.SECONDS));
        runnableCaptor.getValue().run();

        verify(mockDiscoveryListener).onDiscoveryFailedToStart();
    }

    @Test(expected = MissingPermissionException.class)
    public void givenPermissionIsNotGranted_whenDiscoveryIsStarted_thenExceptionIsThrown() throws MissingPermissionException {
        when(ContextCompat.checkSelfPermission(mockContext, ACCESS_COARSE_LOCATION)).thenReturn(PERMISSION_DENIED);

        strategyUnderTest.start();
    }

    @Test
    public void givenScanIsNotStarted_whenScanIsStopped_thenStopIsCalledOnScanner() throws MissingPermissionException {

        strategyUnderTest.stop();

        verify(mockScanner).stopScanning();
    }

    @Test
    public void givenScanIsStarted_whenScanIsStopped_then() throws MissingPermissionException {
        when(mockScanner.startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), eq(DuplicatesAllowed), eq(SCAN_WINDOW_MILLIS))).thenReturn(true);
        ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
        when(mockExecutor.scheduleAtFixedRate(any(Runnable.class), eq(0L), eq(30L), eq(TimeUnit.SECONDS))).thenReturn(scheduledFuture);
        strategyUnderTest.start();

        strategyUnderTest.stop();

        verify(scheduledFuture).cancel(true);
    }

    @Test
    public void whenDeviceCacheIsCleared_thenAllNetworkNodesInCacheWillBeLost() {
        CacheData mockCacheData = mock(CacheData.class);
        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockCacheData.getNetworkNode()).thenReturn(mockNetworkNode);
        ArrayList<CacheData> deviceCaches = new ArrayList<>();
        deviceCaches.add(mockCacheData);
        Collection<CacheData> fakeCollection = unmodifiableCollection(deviceCaches);

        when(mockCache.clear()).thenReturn(fakeCollection);

        strategyUnderTest.clearDiscoveredNetworkNodes();

        verify(mockDiscoveryListener).onNetworkNodeLost(mockNetworkNode);
    }
}
