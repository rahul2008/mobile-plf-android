package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.annotation.NonNull;

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

public class SHNInternalScanRequestTest {

    public static final String TEST_MAC_1 = "TEST_MAC_1";
    public static final String TEST_MAC_2 = "TEST_MAC_2";

    public static final long TIMEOUT_1 = 111;

    public static final String DEVICE_NAME_1 = "DEVICE_NAME_1";
    public static final String DEVICE_NAME_2 = "DEVICE_NAME_2";

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

    @Mock
    private SHNDeviceFoundInfo deviceFoundInfoMock;

    @Mock
    private SHNDevice deviceMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private List<SHNDeviceDefinitionInfo> deviceDefinitions = new ArrayList<>();
    private List<String> macAddresses = new ArrayList<>();

    private SHNInternalScanRequest shnInternalScanRequest;

    @Before
    public void setUp() {
        initMocks(this);

        SHNDeviceFoundInfo.setSHNCentral(shnCentralMock);

        deviceDefinitions.add(definitionInfoMock1);
        deviceDefinitions.add(definitionInfoMock2);

        when(definitionInfoMock1.getDeviceTypeName()).thenReturn(DEVICE_NAME_1);
        when(definitionInfoMock2.getDeviceTypeName()).thenReturn(DEVICE_NAME_2);

        macAddresses.add(TEST_MAC_1);

        shnInternalScanRequest = new SHNInternalScanRequest(deviceDefinitions, macAddresses, true, TIMEOUT_1, scannerListenerMock);
    }

    @After
    public void tearDown() throws Exception {
        SHNDeviceFoundInfo.setSHNCentral(null);
    }

    @Test
    public void ShouldPostRunnableWithDelay_WhenStarted() {
        shnInternalScanRequest.scanningStarted(scannerMock, handlerMock);

        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(TIMEOUT_1));
    }

    @Test
    public void ShouldInformScanner_WhenTimeoutOccurs() {
        Runnable runnable = captureTimeoutRunnable();
        runnable.run();

        verify(scannerMock).stopScanning(shnInternalScanRequest);
    }

    @Test
    public void ShouldRemoveRunnableFromHandler_WhenScanningIsStopped() {
        Runnable runnable = captureTimeoutRunnable();

        shnInternalScanRequest.scanningStopped();

        verify(handlerMock).removeCallbacks(runnable);
    }

    @Test
    public void ShouldReportDevice_WhenMacAddressMatchesRequested() {
        SHNDeviceFoundInfo bleDeviceFoundInfo = prepareOnScanResultInvocation(TEST_MAC_1, DEVICE_NAME_1);

        shnInternalScanRequest.scanningStarted(scannerMock, handlerMock);
        shnInternalScanRequest.onDeviceFound(bleDeviceFoundInfo);

        verify(scannerListenerMock).deviceFound((SHNDeviceScanner) any(), isA(SHNDeviceFoundInfo.class));
    }

    @Test
    public void ShouldIgnoreDevice_WhenMacAddressNotMatchesRequested() {
        SHNDeviceFoundInfo bleDeviceFoundInfo = prepareOnScanResultInvocation(TEST_MAC_2, DEVICE_NAME_2);

        shnInternalScanRequest.scanningStarted(scannerMock, handlerMock);
        shnInternalScanRequest.onDeviceFound(bleDeviceFoundInfo);

        verify(scannerListenerMock, never()).deviceFound(any(SHNDeviceScanner.class), isA(SHNDeviceFoundInfo.class));
    }

    // ------------------

    @NonNull
    private SHNDeviceFoundInfo prepareOnScanResultInvocation(final String macAddress, final String deviceName) {
        int rssi = 1;

        when(deviceFoundInfoMock.getDeviceAddress()).thenReturn(macAddress);
        when(deviceFoundInfoMock.getShnDevice()).thenReturn(deviceMock);
        when(deviceMock.getDeviceTypeName()).thenReturn(deviceName);

        when(definitionInfoMock1.useAdvertisedDataMatcher()).thenReturn(true);
        when(definitionInfoMock1.matchesOnAdvertisedData(any(BluetoothDevice.class), any(BleScanRecord.class), eq(rssi))).thenReturn(true);
        when(definitionInfoMock1.getDeviceTypeName()).thenReturn(deviceName);
        return deviceFoundInfoMock;
    }

    private Runnable captureTimeoutRunnable() {
        shnInternalScanRequest.scanningStarted(scannerMock, handlerMock);
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(TIMEOUT_1));
        return runnableCaptor.getValue();
    }
}
