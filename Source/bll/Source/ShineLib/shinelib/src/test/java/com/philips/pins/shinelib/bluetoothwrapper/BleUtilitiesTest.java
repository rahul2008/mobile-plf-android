/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanSettings;

import com.philips.pins.shinelib.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.N;
import static android.os.Build.VERSION_CODES.N_MR1;
import static android.os.Build.VERSION_CODES.O;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {N_MR1}, shadows = {ShadowBluetoothLEAdapter.class})
public class BleUtilitiesTest {

    private BleUtilities bleUtilities;
    private ShadowBluetoothLEAdapter bluetoothAdapter;
    private ShadowApplication application;

    @Mock
    private BluetoothLeScanner scanner;

    @Mock
    private ScanCallback mockedScanCallback;

    @Before
    public void setUp() {
        initMocks(this);

        application = ShadowApplication.getInstance();

        bluetoothAdapter = Shadow.extract(ShadowBluetoothLEAdapter.getDefaultAdapter());
        bluetoothAdapter.setEnabled(true);
        bluetoothAdapter.setIsOffloadedScanBatchingSupported(true);
        bluetoothAdapter.setBluetoothLeScanner(scanner);

        bleUtilities = new BleUtilities(application.getApplicationContext());
    }

    @Test
    public void whenStartLeScanIsCalledAndBluetoothIsOffThenTheScanIsNotStarted() {
        bluetoothAdapter.setEnabled(false);
        bleUtilities.startLeScan(mockedScanCallback);

        verify(scanner, never()).startScan(anyList(), any(ScanSettings.class), any(ScanCallback.class));
    }

    @Test
    public void whenStartLeScanIsCalledAndBatchScanIsSupportedThenTheScanIsStarted() {
        bleUtilities.startLeScan(mockedScanCallback);

        ArgumentCaptor<ScanSettings> callbackCaptor = ArgumentCaptor.forClass(ScanSettings.class);
        verify(scanner).startScan(anyList(), callbackCaptor.capture(), any(ScanCallback.class));

        List<ScanSettings> capturedMeasurements = callbackCaptor.getAllValues();
        ScanSettings settings = capturedMeasurements.get(0);
        assertEquals(settings.getReportDelayMillis(), 1000);
    }

    @Test
    public void whenStartLeScanIsCalledAndBatchScanIsNotSupportedThenTheScanIsStarted() {
        bluetoothAdapter.setIsOffloadedScanBatchingSupported(false);

        bleUtilities.startLeScan(mockedScanCallback);

        ArgumentCaptor<ScanSettings> callbackCaptor = ArgumentCaptor.forClass(ScanSettings.class);
        verify(scanner).startScan(anyList(), callbackCaptor.capture(), any(ScanCallback.class));

        List<ScanSettings> capturedMeasurements = callbackCaptor.getAllValues();
        ScanSettings settings = capturedMeasurements.get(0);
        assertEquals(settings.getReportDelayMillis(), 0);
    }

    @Test
    public void whenStopLeScanIsCalledThenTheScanIsStopped() {
        bleUtilities.stopLeScan(mockedScanCallback);

        verify(scanner).stopScan(any(ScanCallback.class));
    }

    @Test
    public void whenStopLeScanIsCalledAndBluetoothIsOffThenTheScanIsNotStopped() {
        bluetoothAdapter.setEnabled(false);
        bleUtilities.stopLeScan(mockedScanCallback);

        verify(scanner, never()).stopScan(any(ScanCallback.class));
    }
}

