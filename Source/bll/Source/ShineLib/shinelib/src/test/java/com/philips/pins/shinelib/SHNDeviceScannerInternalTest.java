/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanRecord;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral.SHNCentralListener;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy.LeScanCallback;
import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.utility.BleScanRecord;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateReady;
import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed;
import static com.philips.pins.shinelib.SHNDeviceScannerInternal.SCANNING_RESTART_INTERVAL_MS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doReturn;

public class SHNDeviceScannerInternalTest extends RobolectricTest {

    private static final long STOP_SCANNING_AFTER_10_SECONDS = 10_000L;
    private static final String MOCKED_BLUETOOTH_DEVICE_NAME = "Mocked Bluetooth Device";
    private static final String DEVICE_INFO_SERVICE_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A);

    @Mock
    private SHNDevice deviceMock;

    @Mock
    private SHNCentral shnCentralMock;

    @Mock
    private SHNInternalScanRequest internalScanRequestMock1;

    @Mock
    private SHNInternalScanRequest internalScanRequestMock2;

    @Mock
    private LeScanCallbackProxy leScanCallbackProxyMock;

    @Mock
    private SHNDeviceScanner.SHNDeviceScannerListener mockedSHNDeviceScannerListener;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private MockedHandler mockedHandler;
    private SHNDeviceScannerInternal shnDeviceScannerInternal;

    private boolean resultForMatchesOnAdvertisedData;
    private boolean resultForUseAdvertisedDataMatcher;

    @Captor
    private ArgumentCaptor<LeScanCallback> leScanCallbackCaptor;

    @Captor
    private ArgumentCaptor<SHNDeviceFoundInfo> shnDeviceFoundInfoArgumentCaptor;

    @Captor
    private ArgumentCaptor<SHNCentralListener> shnCentralListenerArgumentCaptor;

    private boolean isScanningStarted;
    private SHNCentralListener shnCentralListenerArgumentCaptorValue;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockedHandler = new MockedHandler();

        when(shnCentralMock.getInternalHandler()).thenReturn(mockedHandler.getMock());
        doReturn(deviceMock).when(shnCentralMock).createSHNDeviceForAddressAndDefinition(anyString(), any(SHNDeviceDefinitionInfo.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                shnCentralListenerArgumentCaptorValue = shnCentralListenerArgumentCaptor.getValue();
                return null;
            }
        }).when(shnCentralMock).registerShnCentralListener(shnCentralListenerArgumentCaptor.capture());

        when(deviceMock.getDeviceTypeName()).thenReturn(MOCKED_BLUETOOTH_DEVICE_NAME);

        List<SHNDeviceDefinitionInfo> testDeviceDefinitionInfos = new ArrayList<>();
        testDeviceDefinitionInfos.add(new SHNDeviceDefinitionInfo() {
            @Override
            public String getDeviceTypeName() {
                return MOCKED_BLUETOOTH_DEVICE_NAME;
            }

            @Override
            public Set<UUID> getPrimaryServiceUUIDs() {
                Set<UUID> primaryServiceUUIDs = new HashSet<>();
                primaryServiceUUIDs.add(UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A)));
                return primaryServiceUUIDs;
            }

            @Override
            public SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener) {
                return null;
            }

            @Override
            public SHNDeviceDefinition getSHNDeviceDefinition() {
                return null;
            }

            @Override
            public boolean useAdvertisedDataMatcher() {
                return resultForUseAdvertisedDataMatcher;
            }

            @Override
            public boolean matchesOnAdvertisedData(BluetoothDevice bluetoothDevice, BleScanRecord bleScanRecord, int rssi) {
                return resultForMatchesOnAdvertisedData;
            }

            @Override
            public int getConnectionPriority() {
                return BluetoothGatt.CONNECTION_PRIORITY_BALANCED;
            }
        });

        shnDeviceScannerInternal = new TestSHNDeviceScannerInternal(shnCentralMock, testDeviceDefinitionInfos);
    }

    @Test
    public void givenScanningIsStarted_thenStartLeScanOnTheLeProxyIsCalled() {
        startScanning();

        assertThat(isScanningStarted).isTrue();

        verify(leScanCallbackProxyMock).startLeScan(any(LeScanCallback.class));
    }

    @Test
    public void givenScanningIsStarted_whenSHNCentralIsNotReady_thenScanningIsNotRestarted() {
        startScanning();

        shnCentralListenerArgumentCaptorValue.onStateUpdated(shnCentralMock, SHNCentralStateNotReady);

        verify(leScanCallbackProxyMock, never()).stopLeScan(any(LeScanCallback.class));
        verify(leScanCallbackProxyMock, times(1)).startLeScan(any(LeScanCallback.class));
    }

    @Test
    public void givenScanningIsStarted_whenSHNCentralIsReady_thenScanningIsRestarted() {
        startScanning();

        shnCentralListenerArgumentCaptorValue.onStateUpdated(shnCentralMock, SHNCentralStateReady);

        verify(leScanCallbackProxyMock).stopLeScan(any(LeScanCallback.class));
        verify(leScanCallbackProxyMock, times(2)).startLeScan(any(LeScanCallback.class));
    }

    @Test
    public void givenScanningIsStarted_whenScanningIsStartedAgain_thenStartLeScanIsNotCalledTwice() {
        startScanning();

        startScanning();

        assertThat(isScanningStarted).isTrue();
        verify(leScanCallbackProxyMock).startLeScan(any(LeScanCallback.class));
    }

    @Test
    public void givenScanningIsStarted_whenScanningIsStopped_thenStopLeScanOnTheLeProxyIsCalledWithTheSameCallback() {
        startScanning();
        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());

        shnDeviceScannerInternal.onStopScanning();
        ArgumentCaptor<LeScanCallback> leScanCallbackStopArgumentCaptor = ArgumentCaptor.forClass(LeScanCallback.class);
        verify(leScanCallbackProxyMock).stopLeScan(leScanCallbackStopArgumentCaptor.capture());

        assertEquals(leScanCallbackCaptor.getValue(), leScanCallbackStopArgumentCaptor.getValue());
    }

    @Test
    public void givenScanningIsStarted_whenScanningIsStopped_thenScanStoppedOnTheListenerIsCalled() {
        startScanning();

        shnDeviceScannerInternal.onStopScanning();

        verify(mockedSHNDeviceScannerListener).scanStopped(null);
    }

    @Test
    public void givenScanningIsStarted_whenTimeoutOccurs_thenScanStoppedOnTheListenerIsCalled() {
        startScanning();

        mockedHandler.executeFirstScheduledExecution();

        verify(mockedSHNDeviceScannerListener).scanStopped(null);
    }

    @Test
    public void whenDuringScanningADeviceIsFoundWithMatchingPrimaryServiceUUID16_ThenItIsReported() {
        startScanning();

        BluetoothDevice mockedBluetoothDevice = Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("12:34:56:78:90:AB").when(mockedBluetoothDevice).getAddress();
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedBluetoothDevice).getName();

        ScanRecord mockedScanRecord = Utility.makeThrowingMock(ScanRecord.class);
        doReturn(new byte[]{0x03, 0x03, 0x0A, 0x18}).when(mockedScanRecord).getBytes(); // advertisement of the primary uuid for the device info service
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedScanRecord).getDeviceName();

        final List<ParcelUuid> uuids = new ArrayList<>();
        uuids.add(ParcelUuid.fromString(DEVICE_INFO_SERVICE_UUID));
        doReturn(uuids).when(mockedScanRecord).getServiceUuids();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());

        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 50, mockedScanRecord);

        verify(mockedSHNDeviceScannerListener, atLeastOnce()).deviceFound((SHNDeviceScanner) any(), shnDeviceFoundInfoArgumentCaptor.capture());
        assertEquals("12:34:56:78:90:AB", shnDeviceFoundInfoArgumentCaptor.getValue().getDeviceAddress());
        assertEquals(MOCKED_BLUETOOTH_DEVICE_NAME, shnDeviceFoundInfoArgumentCaptor.getValue().getDeviceName());
        assertArrayEquals(mockedScanRecord.getBytes(), shnDeviceFoundInfoArgumentCaptor.getValue().getScanRecord());
        assertEquals(50, shnDeviceFoundInfoArgumentCaptor.getValue().getRssi());
    }

    @Test
    public void whenDuringScanningADeviceIsFoundMultipleTimesWithMatchingPrimaryServiceUUID16_ThenItIsReportedOnlyOnce() {
        startScanning();

        BluetoothDevice mockedBluetoothDevice = Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("12:34:56:78:90:AB").when(mockedBluetoothDevice).getAddress();
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedBluetoothDevice).getName();

        ScanRecord mockedScanRecord = Utility.makeThrowingMock(ScanRecord.class);
        doReturn(new byte[]{0x03, 0x03, 0x0A, 0x18}).when(mockedScanRecord).getBytes(); // advertisement of the primary uuid for the device info service
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedScanRecord).getDeviceName();

        final List<ParcelUuid> uuids = new ArrayList<>();
        uuids.add(ParcelUuid.fromString(DEVICE_INFO_SERVICE_UUID));
        doReturn(uuids).when(mockedScanRecord).getServiceUuids();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());

        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 0, mockedScanRecord);
        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 0, mockedScanRecord);

        verify(mockedSHNDeviceScannerListener, times(1)).deviceFound((SHNDeviceScanner) any(), shnDeviceFoundInfoArgumentCaptor.capture()); // Note that verify checks that the callback is called only once!
    }

    @Test
    public void whenDuringScanningADeviceIsFoundWithMatchingPrimaryServiceUUID128_ThenItIsReported() {
        startScanning();

        BluetoothDevice mockedBluetoothDevice = Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("12:34:56:78:90:AB").when(mockedBluetoothDevice).getAddress();
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedBluetoothDevice).getName();

        ScanRecord mockedScanRecord = Utility.makeThrowingMock(ScanRecord.class);
        doReturn(new byte[]{(byte) 0x10, // len
                (byte) 0x07, // type
                (byte) 0xFB, (byte) 0x34, (byte) 0x9B, (byte) 0x5F, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x0A, (byte) 0x18, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xFF, (byte) 0x0A, (byte) 0x18, // unhandled type with data length 3!
                (byte) 0x00, (byte) 0x00, (byte) 0x00}).when(mockedScanRecord).getBytes(); // len = 0 // advertisement of the primary uuid for the device info service

        final List<ParcelUuid> uuids = new ArrayList<>();
        uuids.add(ParcelUuid.fromString(DEVICE_INFO_SERVICE_UUID));
        doReturn(uuids).when(mockedScanRecord).getServiceUuids();
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedScanRecord).getDeviceName();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());

        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 0, mockedScanRecord);

        verify(mockedSHNDeviceScannerListener).deviceFound((SHNDeviceScanner) any(), any(SHNDeviceFoundInfo.class));
    }

    @Test
    public void whenDuringScanningADeviceIsFoundWithNOTMatchingPrimaryServiceUUID16_ThenItIsNotReported() {
        resultForUseAdvertisedDataMatcher = false;

        startScanning();

        BluetoothDevice mockedBluetoothDevice = Utility.makeThrowingMock(BluetoothDevice.class);

        ScanRecord mockedScanRecord = Utility.makeThrowingMock(ScanRecord.class);
        List<ParcelUuid> uuids = new ArrayList<>();
        uuids.add(ParcelUuid.fromString("0000000-1234-1234-1234-000000000000"));
        doReturn(uuids).when(mockedScanRecord).getServiceUuids();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());
        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 0, mockedScanRecord);

        verify(mockedSHNDeviceScannerListener, never()).deviceFound(any(SHNDeviceScanner.class), any(SHNDeviceFoundInfo.class));
    }

    @Test
    public void whenStopScanningIsCalledWhenNotScanning_ThenNothingBadHappens() {
        shnDeviceScannerInternal.onStopScanning();
    }

    @Test
    public void whenThePluginScannerDataMatcherReturnsTrue_ThenTheScannerReportsDeviceFound() {
        resultForUseAdvertisedDataMatcher = true;
        resultForMatchesOnAdvertisedData = true;

        startScanning();

        BluetoothDevice mockedBluetoothDevice = Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("12:34:56:78:90:AB").when(mockedBluetoothDevice).getAddress();
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedBluetoothDevice).getName();

        ScanRecord mockedScanRecord = Utility.makeThrowingMock(ScanRecord.class);
        doReturn(new byte[]{0x03, 0x03, 0x0A, 0x17}).when(mockedScanRecord).getBytes(); // advertisement of the primary uuid for an unknown service
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedScanRecord).getDeviceName();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());

        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 0, mockedScanRecord);

        verify(mockedSHNDeviceScannerListener).deviceFound((SHNDeviceScanner) any(), isA(SHNDeviceFoundInfo.class));
    }

    @Test
    public void whenThePluginScannerDataMatcherReturnsFalse_ThenTheScannerDoesNotReportTheDeviceFound() {
        resultForUseAdvertisedDataMatcher = true;
        resultForMatchesOnAdvertisedData = false;

        startScanning();

        BluetoothDevice mockedBluetoothDevice = Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("12:34:56:78:90:AB").when(mockedBluetoothDevice).getAddress();
        doReturn(MOCKED_BLUETOOTH_DEVICE_NAME).when(mockedBluetoothDevice).getName();

        ScanRecord mockedScanRecord = Utility.makeThrowingMock(ScanRecord.class);
        doReturn(new byte[]{}).when(mockedScanRecord).getBytes();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());
        leScanCallbackCaptor.getValue().onScanResult(mockedBluetoothDevice, 0, mockedScanRecord);

        verify(mockedSHNDeviceScannerListener, never()).deviceFound(any(SHNDeviceScanner.class), isA(SHNDeviceFoundInfo.class));
    }

    @Test
    public void whenScanningTwice_ThenBothScanRequestGetStartedMessage() {
        shnDeviceScannerInternal.startScanning(internalScanRequestMock1);
        shnDeviceScannerInternal.startScanning(internalScanRequestMock2);

        verify(internalScanRequestMock1).onScanningStarted(shnDeviceScannerInternal, mockedHandler.getMock());
        verify(internalScanRequestMock2).onScanningStarted(shnDeviceScannerInternal, mockedHandler.getMock());
    }

    @Test
    public void whenScanningTwice_ThenWhenOneIsStoppedScanningItShouldBeInformed() {
        shnDeviceScannerInternal.startScanning(internalScanRequestMock1);
        shnDeviceScannerInternal.startScanning(internalScanRequestMock2);

        Handler handlerMock = mockedHandler.getMock();
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(SCANNING_RESTART_INTERVAL_MS));

        shnDeviceScannerInternal.stopScanning(internalScanRequestMock1);
        verify(internalScanRequestMock1).onScanningStopped();
    }

    @Test
    public void whenScanningTwice_ThenWhenOneIsStoppedScanningShouldContinueForTheOther() {
        shnDeviceScannerInternal.startScanning(internalScanRequestMock1);
        shnDeviceScannerInternal.startScanning(internalScanRequestMock2);

        Handler handlerMock = mockedHandler.getMock();
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(SCANNING_RESTART_INTERVAL_MS));

        shnDeviceScannerInternal.stopScanning(internalScanRequestMock1);
        verify(internalScanRequestMock1).onScanningStopped();
    }

    @Test
    public void whenScanningTwice_ThenWhenBothAreStoppedScanningShouldStop() {
        shnDeviceScannerInternal.startScanning(internalScanRequestMock1);
        shnDeviceScannerInternal.startScanning(internalScanRequestMock2);

        Handler handlerMock = mockedHandler.getMock();
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(SCANNING_RESTART_INTERVAL_MS));

        shnDeviceScannerInternal.stopScanning(internalScanRequestMock1);
        shnDeviceScannerInternal.stopScanning(internalScanRequestMock2);

        verify(handlerMock).removeCallbacks(runnableCaptor.getValue());
    }

    @Test
    public void whenScanIsRestartedAfterScanningScanFailed_ThenNewScanIsStarted() {
        startScanning();

        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());

        leScanCallbackCaptor.getValue().onScanFailed(0);

        Handler handlerMock = mockedHandler.getMock();
        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        startScanning();
        verify(leScanCallbackProxyMock, times(2)).startLeScan(any(LeScanCallback.class));
    }

    @Test
    public void whenScanning_ThenRestartIsPerformedEvery30Seconds() {
        shnDeviceScannerInternal.startScanning(internalScanRequestMock1);
        shnDeviceScannerInternal.startScanning(internalScanRequestMock2);

        Handler handlerMock = mockedHandler.getMock();
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(SCANNING_RESTART_INTERVAL_MS));
        reset(leScanCallbackProxyMock);

        runnableCaptor.getValue().run();
        InOrder inOrder = Mockito.inOrder(leScanCallbackProxyMock);
        inOrder.verify(leScanCallbackProxyMock).stopLeScan(any(LeScanCallback.class));
        inOrder.verify(leScanCallbackProxyMock).startLeScan(any(LeScanCallback.class));
    }

    private void startScanning() {
        isScanningStarted = shnDeviceScannerInternal.startScanning(mockedSHNDeviceScannerListener, DuplicatesNotAllowed, STOP_SCANNING_AFTER_10_SECONDS);
    }

    private class TestSHNDeviceScannerInternal extends SHNDeviceScannerInternal {

        TestSHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
            super(shnCentral, registeredDeviceDefinitions);
        }

        @Override
        LeScanCallbackProxy createLeScanCallbackProxy() {
            return leScanCallbackProxyMock;
        }
    }
}
