/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.associationprocedures.SHNAssociationProcedureNearestDevice;
import com.philips.pins.shinelib.framework.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Timer.class})
public class SHNAssociationProcedureNearestDeviceTestBreakerTest {
    private SHNAssociationProcedureNearestDevice associationProcedure;
    private SHNAssociationProcedurePlugin.SHNAssociationProcedureListener mockedSHNAssociationProcedureListener;
    private Timer mockedIterationTimeoutTimer;
    private List<AbstractMap.Entry<SHNDevice, Integer>> mockedDevicesAndAssociatedRSSI;
    ArgumentCaptor<Runnable> iterationTimeoutTimerRunnable;

    private void createMockedDevices(Integer[] RSSIs) {
        mockedDevicesAndAssociatedRSSI = new ArrayList<>();
        for (Integer rssi : RSSIs) {
            SHNDevice mockedSHNDevice = mock(SHNDevice.class);
            when(mockedSHNDevice.getAddress()).thenReturn("fake-mac-address-" + mockedDevicesAndAssociatedRSSI.size());
            mockedDevicesAndAssociatedRSSI.add(new AbstractMap.SimpleEntry<>(mockedSHNDevice, rssi));
        }
    }

    private void discoverMockedDevices(Integer[] locations) {
        for (Integer location : locations) {
            if (location != null) {
                assertTrue(location < mockedDevicesAndAssociatedRSSI.size());
                AbstractMap.SimpleEntry<SHNDevice, Integer> entry = (AbstractMap.SimpleEntry<SHNDevice, Integer>) mockedDevicesAndAssociatedRSSI.get(location);
                SHNDeviceFoundInfo mockedDeviceFoundInfo = mock(SHNDeviceFoundInfo.class);
                when(mockedDeviceFoundInfo.getRssi()).thenReturn(entry.getValue());
                associationProcedure.deviceDiscovered(entry.getKey(), mockedDeviceFoundInfo);
            }

        }
    }

    private void discoverMockedDevice(int location) {
        discoverMockedDevices(new Integer[]{location});
    }

    private void verifyAssociationSuccess(int location) {
        assertTrue(location < mockedDevicesAndAssociatedRSSI.size());
        AbstractMap.SimpleEntry<SHNDevice, Integer> entry = (AbstractMap.SimpleEntry<SHNDevice, Integer>) mockedDevicesAndAssociatedRSSI.get(location);
        verify(mockedSHNAssociationProcedureListener, times(1)).onAssociationSuccess(any(SHNDevice.class));
        verify(mockedSHNAssociationProcedureListener).onAssociationSuccess(entry.getKey());
    }

    @Before
    public void setUp() {
        mockedIterationTimeoutTimer = mock(Timer.class);
        mockedSHNAssociationProcedureListener = mock(SHNAssociationProcedurePlugin.SHNAssociationProcedureListener.class);

        mockStatic(Timer.class);
        when(Timer.createTimer(any(Runnable.class), eq(SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS))).thenReturn(mockedIterationTimeoutTimer);

        associationProcedure = new SHNAssociationProcedureNearestDevice(mockedSHNAssociationProcedureListener);
        associationProcedure.start();

        PowerMockito.verifyStatic();
        iterationTimeoutTimerRunnable = ArgumentCaptor.forClass(Runnable.class);
        Timer.createTimer(iterationTimeoutTimerRunnable.capture(), eq(SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS));
    }

    @Test
    public void canCreateAssociationProcedure() {
        assertNotNull(associationProcedure);
    }

    @Test
    public void shouldScanShouldReturnTrue() {
        assertTrue(associationProcedure.getShouldScan());
    }

    @Test
    public void shouldKeepRestartingTimerAfterAnIterationUntilMaxIterationCountWasReached() {
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT; ++i) {
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verify(mockedIterationTimeoutTimer, times(SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT)).restart();
    }

    @Test
    public void shouldCallAssociationFailedWhenNoDevicesAreDiscoveredInAllIterations() {
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT; ++i) {
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verify(mockedSHNAssociationProcedureListener).onAssociationFailed(null, SHNResult.SHNErrorAssociationFailed);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationSuccess(any(SHNDevice.class));
    }

    @Test
    public void shouldCallAssociationFailedWhenNoDeviceIsDeemedNearestEnoughTimesSuccessively() {
        createMockedDevices(new Integer[]{-20, -30, -80, -43});
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT; ++i) {
            discoverMockedDevice(i % 4);
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verify(mockedSHNAssociationProcedureListener).onAssociationFailed(null, SHNResult.SHNErrorAssociationFailed);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationSuccess(any(SHNDevice.class));
    }

    @Test
    public void shouldIgnoreDevicesForWhichRSSIIsZeroAndCallAssociationFailed() {
        createMockedDevices(new Integer[]{0});
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT; ++i) {
            discoverMockedDevice(0);
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verify(mockedSHNAssociationProcedureListener).onAssociationFailed(null, SHNResult.SHNErrorAssociationFailed);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationSuccess(any(SHNDevice.class));
    }

    @Test
    public void shouldCallAssociationSucceededWhenDeviceIsImmediatelyDeemedNearestEnoughTimesSuccessively() {
        createMockedDevices(new Integer[]{-20});
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT; ++i) {
            discoverMockedDevice(0);
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verifyAssociationSuccess(0);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationFailed(any(SHNDevice.class), any(SHNResult.class));
    }

    @Test
    public void shouldCallAssociationSucceededWhenDeviceIsLaterDeemedNearestEnoughTimesSuccessively() {
        createMockedDevices(new Integer[]{-20, -1});
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT; ++i) {
            discoverMockedDevice((i > 1) ? 1 : 0);
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verifyAssociationSuccess(1);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationFailed(any(SHNDevice.class), any(SHNResult.class));
    }

    @Test
    public void shouldIgnoreDevicesForWhichNearestDeviceIsNullAndCallAssociationFailed() {
        createMockedDevices(new Integer[]{-10, -20, -80, -43, -1});
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT; ++i) {
            discoverMockedDevices(new Integer[]{null, 1, 2, 3, 4});
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verify(mockedSHNAssociationProcedureListener).onAssociationFailed(null, SHNResult.SHNErrorAssociationFailed);

    }

    @Test
    public void shouldDeemTheDeviceWithTheHighestRSSITheNearestDevice() {
        createMockedDevices(new Integer[]{-10, -20, -80, -43, -1});
        for (int i = 0; i < SHNAssociationProcedureNearestDevice.ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT; ++i) {
            discoverMockedDevices(new Integer[]{0, 1, 2, 3, 4});
            iterationTimeoutTimerRunnable.getValue().run();
        }
        verifyAssociationSuccess(4);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationFailed(any(SHNDevice.class), any(SHNResult.class));
    }

    @Test
    public void shouldCallAssociationFailedAndStopTheTimeoutTimerWhenScannerTimesOut() {
        associationProcedure.scannerTimeout();

        verify(mockedIterationTimeoutTimer).stop();
        verify(mockedSHNAssociationProcedureListener).onAssociationFailed(null, SHNResult.SHNErrorTimeout);
        verify(mockedSHNAssociationProcedureListener, never()).onAssociationSuccess(any(SHNDevice.class));
    }

    @Test
    public void shouldStopTimerWhenStopIsCalled() {
        associationProcedure.stop();

        verify(mockedIterationTimeoutTimer).stop();
    }
}
