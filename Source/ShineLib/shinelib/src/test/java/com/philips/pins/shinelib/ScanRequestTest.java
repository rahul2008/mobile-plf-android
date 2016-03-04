package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.utility.BleScanRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScanRequestTest {

    public static final String TEST_MAC_1 = "TEST_MAC_1";
    public static final String TEST_MAC_2 = "TEST_MAC_2";

    public static final long TIMEOUT_1 = 111;

    @Mock
    private SHNCentral shnCentralMock;

    @Mock
    private SHNDeviceDefinitionInfo definitionInfoMock1;

    @Mock
    private SHNDeviceDefinitionInfo definitionInfoMock2;

    @Mock
    private SHNDeviceScanner.SHNDeviceScannerListener scannerListenerMock;

    @Mock
    private SHNDeviceScannerInternal scannerMock;

    @Mock
    private Handler handlerMock;

    @Mock
    private BluetoothDevice bluetoothDeviceMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private List<SHNDeviceDefinitionInfo> deviceDefinitions = new ArrayList<>();
    private List<String> macAddresses = new ArrayList<>();

    private ScanRequest scanRequest;

    @Before
    public void setUp() {
        initMocks(this);

        SHNDeviceFoundInfo.setSHNCentral(shnCentralMock);

        deviceDefinitions.add(definitionInfoMock1);
        deviceDefinitions.add(definitionInfoMock2);

        macAddresses.add(TEST_MAC_1);

        scanRequest = new ScanRequest(deviceDefinitions, macAddresses, true, TIMEOUT_1, scannerListenerMock);
    }

    @After
    public void tearDown() throws Exception {
        SHNDeviceFoundInfo.setSHNCentral(null);
    }

    @Test
    public void ShouldPostRunnableWithDelay_WhenStarted() {
        scanRequest.scanningStarted(scannerMock, handlerMock);

        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(TIMEOUT_1));
    }

    @Test
    public void ShouldInformScanner_WhenTimeoutOccurs() {
        Runnable runnable = captureTimeoutRunnable();
        runnable.run();

        verify(scannerMock).stopScanning(scanRequest);
    }

    @Test
    public void ShouldRemoveRunnableFromHandler_WhenScanningIsStopped() {
        Runnable runnable = captureTimeoutRunnable();

        scanRequest.scanningStopped();

        verify(handlerMock).removeCallbacks(runnable);
    }

    @Test
    public void ShouldReportDevice_WhenMacAddressMatchesRequested() {
        BleDeviceFoundInfo bleDeviceFoundInfo = prepareOnScanResultInvocation(TEST_MAC_1);

        scanRequest.scanningStarted(scannerMock, handlerMock);
        scanRequest.onScanResult(bleDeviceFoundInfo);

        verify(scannerListenerMock).deviceFound(any(SHNDeviceScanner.class), isA(SHNDeviceFoundInfo.class));
    }

    @Test
    public void ShouldIgnoreDevice_WhenMacAddressNotMatchesRequested() {
        BleDeviceFoundInfo bleDeviceFoundInfo = prepareOnScanResultInvocation(TEST_MAC_2);

        scanRequest.scanningStarted(scannerMock, handlerMock);
        scanRequest.onScanResult(bleDeviceFoundInfo);

        verify(scannerListenerMock, never()).deviceFound(any(SHNDeviceScanner.class), isA(SHNDeviceFoundInfo.class));
    }

    // ------------------

    @NonNull
    private BleDeviceFoundInfo prepareOnScanResultInvocation(final String macAddress) {
        int rssi = 1;
        BleDeviceFoundInfo bleDeviceFoundInfo = new BleDeviceFoundInfo(bluetoothDeviceMock, rssi, new byte[]{0x11, 0x22});
        when(bluetoothDeviceMock.getAddress()).thenReturn(macAddress);

        when(definitionInfoMock1.useAdvertisedDataMatcher()).thenReturn(true);
        when(definitionInfoMock1.matchesOnAdvertisedData(any(BluetoothDevice.class), any(BleScanRecord.class), eq(rssi))).thenReturn(true);
        return bleDeviceFoundInfo;
    }

    private Runnable captureTimeoutRunnable() {
        scanRequest.scanningStarted(scannerMock, handlerMock);
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(TIMEOUT_1));
        return runnableCaptor.getValue();
    }
}
