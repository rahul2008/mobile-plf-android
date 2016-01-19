package com.philips.pins.shinelib;

import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by 310188215 on 19/01/16.
 */
public class SHNDeviceScannerTest {

    public static final long SCAN_TIMEOUT_MS = 30000;

    @Mock
    SHNDeviceScannerInternal mockedSHNDeviceScannerInternal;

    @Mock
    SHNDeviceScanner.SHNDeviceScannerListener mockedSHNDeviceScannerListener;

    @Captor
    ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> captureSHNDeviceScannerListener;

    private MockedHandler mockedUserHandler;
    private MockedHandler mockedInternalHandler;

    private SHNDeviceScanner shnDeviceScanner;

    @Before
    public void setUp() {
        initMocks(this);
        mockedUserHandler = new MockedHandler();
        mockedInternalHandler = new MockedHandler();
        mockedUserHandler.enableImmediateExecuteOnPost(false);
        mockedInternalHandler.enableImmediateExecuteOnPost(false);

        when(mockedSHNDeviceScannerInternal.startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong())).thenReturn(true);
        shnDeviceScanner = new SHNDeviceScanner(mockedSHNDeviceScannerInternal, mockedInternalHandler.getMock(), mockedUserHandler.getMock());
    }

    @Test
    public void canCreateInstance() {
        assertNotNull(shnDeviceScanner);
    }

    @Test
    public void testStartScanningWithFuturePostsStartScanningOnTheInternalHandler() throws Exception {

        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());

        /* start scanning */
        FutureTask<Boolean> future = shnDeviceScanner.startScanningWithFuture(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);

        /* verify that it is posted on the internal handler, but not executed yet */
        assertEquals(1, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal, never()).startScanning(isA(SHNDeviceScanner.SHNDeviceScannerListener.class), isA(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
        assertFalse(future.isDone());

        /* execute on the internal handler */
        mockedInternalHandler.executeFirstPostedExecution();
        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal).startScanning(captureSHNDeviceScannerListener.capture(), isA(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
        assertTrue(future.isDone());

        /* get the result */
        assertTrue(future.get());
    }

    @Test
    public void scannerCallbacksArePostedOnTheUserHandler() throws Exception {
        FutureTask<Boolean> future = shnDeviceScanner.startScanningWithFuture(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);
        mockedInternalHandler.executeFirstPostedExecution();
        verify(mockedSHNDeviceScannerInternal).startScanning(captureSHNDeviceScannerListener.capture(), isA(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());

        assertEquals(0, mockedUserHandler.getPostedExecutionCount());

        SHNDeviceScanner.SHNDeviceScannerListener internalDeviceScannerListener = captureSHNDeviceScannerListener.getValue();
        assertNotNull(internalDeviceScannerListener);

        internalDeviceScannerListener.deviceFound(null, null);
        assertEquals(1, mockedUserHandler.getPostedExecutionCount());
        mockedUserHandler.executeFirstPostedExecution();
        verify(mockedSHNDeviceScannerListener).deviceFound(shnDeviceScanner, null);

        assertEquals(0, mockedUserHandler.getPostedExecutionCount());
        internalDeviceScannerListener.scanStopped(null);
        assertEquals(1, mockedUserHandler.getPostedExecutionCount());
        mockedUserHandler.executeFirstPostedExecution();
        verify(mockedSHNDeviceScannerListener).scanStopped(shnDeviceScanner);
    }

    @Test
    public void startingScanningReturnsTheResultFromTheInternalScanner() throws ExecutionException, InterruptedException {
        when(mockedSHNDeviceScannerInternal.startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong())).thenReturn(false);
        FutureTask<Boolean> future1 = shnDeviceScanner.startScanningWithFuture(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);
        mockedInternalHandler.executeFirstPostedExecution();
        assertTrue(future1.isDone());
        assertFalse(future1.get());
    }

    @Test
    public void testStopScanning() throws Exception {

        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());

        /* start scanning */
        shnDeviceScanner.stopScanning();

        /* verify that it is posted on the internal handler, but not executed yet */
        assertEquals(1, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal, never()).stopScanning();

        /* execute on the internal handler */
        mockedInternalHandler.executeFirstPostedExecution();
        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal).stopScanning();
    }
}