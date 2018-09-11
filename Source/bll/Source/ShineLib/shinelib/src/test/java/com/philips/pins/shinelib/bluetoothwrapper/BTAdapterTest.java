/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import com.philips.pins.shinelib.BuildConfig;
import com.philips.pins.shinelib.helper.MockedHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {M}, shadows = {ShadowBluetoothLEAdapter.class})
public class BTAdapterTest {

    private BTAdapter btAdapter;

    private ShadowBluetoothLEAdapter bluetoothAdapter;

    @Mock
    private BluetoothLeScanner scanner;

    @Mock
    private BluetoothDevice bluetoothDevice;

    @Mock
    private ScanCallback mockedScanCallback;

    @Before
    public void setUp() {
        initMocks(this);

        bluetoothAdapter = Shadow.extract(ShadowBluetoothLEAdapter.getDefaultAdapter());
        bluetoothAdapter.setEnabled(true);
        bluetoothAdapter.setIsOffloadedScanBatchingSupported(true);
        bluetoothAdapter.setBluetoothLeScanner(scanner);

        MockedHandler handler = new MockedHandler();
        btAdapter = new BTAdapter(handler.getMock());
    }

    @Test
    public void whenStartLeScanIsCalledAndBluetoothIsOffThenTheScanIsNotStarted() {
        bluetoothAdapter.setEnabled(false);
        btAdapter.startLeScan(mockedScanCallback);

        verify(scanner, never()).startScan(ArgumentMatchers.<ScanFilter>anyList(), any(ScanSettings.class), any(ScanCallback.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenStartLeScanIsCalledAndBatchScanIsSupportedThenTheScanIsStarted() {
        btAdapter.startLeScan(mockedScanCallback);

        ArgumentCaptor<ScanSettings> callbackCaptor = ArgumentCaptor.forClass(ScanSettings.class);
        verify(scanner).startScan((List<ScanFilter>) any(), callbackCaptor.capture(), any(ScanCallback.class));

        List<ScanSettings> capturedMeasurements = callbackCaptor.getAllValues();
        ScanSettings settings = capturedMeasurements.get(0);
        assertEquals(settings.getReportDelayMillis(), 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenStartLeScanIsCalledAndBatchScanIsNotSupportedThenTheScanIsStarted() {
        bluetoothAdapter.setIsOffloadedScanBatchingSupported(false);

        btAdapter.startLeScan(mockedScanCallback);

        ArgumentCaptor<ScanSettings> callbackCaptor = ArgumentCaptor.forClass(ScanSettings.class);
        verify(scanner).startScan((List<ScanFilter>) any(), callbackCaptor.capture(), any(ScanCallback.class));

        List<ScanSettings> capturedMeasurements = callbackCaptor.getAllValues();
        ScanSettings settings = capturedMeasurements.get(0);
        assertEquals(settings.getReportDelayMillis(), 0);
    }

    @Test
    public void whenStopLeScanIsCalledThenTheScanIsStopped() {
        btAdapter.stopLeScan(mockedScanCallback);

        verify(scanner).stopScan(any(ScanCallback.class));
    }

    @Test
    public void whenStopLeScanIsCalledAndBluetoothIsOffThenTheScanIsNotStopped() {
        bluetoothAdapter.setEnabled(false);
        btAdapter.stopLeScan(mockedScanCallback);

        verify(scanner, never()).stopScan(any(ScanCallback.class));
    }

    @Test
    public void whenRemoteDeviceIsRequested_thenBtDeviceIsCreated() {
        final String macAddress = "00:11:22:33:44:55";
        bluetoothAdapter.setRemoteDevice(macAddress, bluetoothDevice);

        final BTDevice remoteDevice = btAdapter.getRemoteDevice(macAddress);

        assertNotNull(remoteDevice);
    }
}

