package com.philips.pins.shinelib;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScanRequestTest {

    public static final String TEST_MAC_1 = "TEST_MAC_1";
    public static final String TEST_MAC_2 = "TEST_MAC_2";

    public static final long TIMEOUT_1 = 111;

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

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private List<SHNDeviceDefinitionInfo> deviceDefinitions = new ArrayList<>();
    private List<String> macAddresses = new ArrayList<>();

    private ScanRequest scanRequest;

    @Before
    public void setUp() {
        initMocks(this);

        deviceDefinitions.add(definitionInfoMock1);
        deviceDefinitions.add(definitionInfoMock2);

        macAddresses.add(TEST_MAC_1);
        macAddresses.add(TEST_MAC_2);

        scanRequest = new ScanRequest(deviceDefinitions, macAddresses, true, TIMEOUT_1, scannerListenerMock);
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
    // ------------------

    private Runnable captureTimeoutRunnable() {
        scanRequest.scanningStarted(scannerMock, handlerMock);
        verify(handlerMock).postDelayed(runnableCaptor.capture(), eq(TIMEOUT_1));
        return runnableCaptor.getValue();
    }
}
