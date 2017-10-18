/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;

import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LeScanCallbackProxyTest {

    private LeScanCallbackProxy leScanCallbackProxy;

    @Mock
    private LeScanCallbackProxy.LeScanCallback mockedLeScanCallback;

    @Mock
    private BleUtilities mockedBleUtilities;

    @Mock
    private ScanResult mockedScanResult;

    @Mock
    private BluetoothDevice mockedBluetoothDevice;

    @Mock
    private ScanRecord mockedScanRecord;

    @Before
    public void setUp() {
        initMocks(this);

        leScanCallbackProxy = new LeScanCallbackProxy(mockedBleUtilities);

        doReturn(mockedBluetoothDevice).when(mockedScanResult).getDevice();
        doReturn(mockedScanRecord).when(mockedScanResult).getScanRecord();
        doReturn(1).when(mockedScanResult).getRssi();
    }

    @Test
    public void whenStartLeScanIsCalledThenTheScannerIsStarted() {
        leScanCallbackProxy.startLeScan(mockedLeScanCallback);
        verify(mockedBleUtilities).startLeScan(any(ScanCallback.class));
    }

    @Test
    public void whenStopLeScanIsCalledAndTheScannerIsScanningThenTheScannerIsStopped() {
        whenStartLeScanIsCalledThenTheScannerIsStarted();
        leScanCallbackProxy.stopLeScan(mockedLeScanCallback);
        verify(mockedBleUtilities).stopLeScan(any(ScanCallback.class));
    }

    @Test
    public void whenStopLeScanIsCalledAndTheScannerIsNotScanningThenTheScannerIsNotStopped2() {
        leScanCallbackProxy.stopLeScan(mockedLeScanCallback);
        verify(mockedBleUtilities, never()).stopLeScan(any(ScanCallback.class));
    }

    @Test
    public void whenStopLeScanIsCalledWithWrongCallbackAndTheScannerIsScanningThenTheScannerIsNotStopped() {
        whenStartLeScanIsCalledThenTheScannerIsStarted();
        LeScanCallbackProxy.LeScanCallback otherLeScanCallback = mock(LeScanCallbackProxy.LeScanCallback.class);
        leScanCallbackProxy.stopLeScan(otherLeScanCallback);
        verify(mockedBleUtilities, never()).stopLeScan(any(ScanCallback.class));
    }

    @Test
    public void whenAScanResultIsReceivedThenTheCallbackIsNotified() {
        whenStartLeScanIsCalledThenTheScannerIsStarted();
        leScanCallbackProxy.onScanResult(0, mockedScanResult);
        verify(mockedLeScanCallback).onScanResult(mockedBluetoothDevice, 1, mockedScanRecord);
    }

    @Test
    public void whenAScanResultIsReceivedInBatchThenTheCallbackIsNotified() {
        whenStartLeScanIsCalledThenTheScannerIsStarted();

        List<ScanResult> results = new ArrayList<>();
        results.add(mockedScanResult);
        results.add(mockedScanResult);

        leScanCallbackProxy.onBatchScanResults(results);
        verify(mockedLeScanCallback, times(2)).onScanResult(mockedBluetoothDevice, 1, mockedScanRecord);
    }

    @Test
    public void whenTheScanFailedThenTheCallbackIsNotified() {
        whenStartLeScanIsCalledThenTheScannerIsStarted();

        leScanCallbackProxy.onScanFailed(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
        verify(mockedLeScanCallback).onScanFailed(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED);
    }
}
