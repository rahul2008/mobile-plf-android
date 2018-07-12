/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.tagging.SHNTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class SHNDeviceScannerTest {

    private static final long SCAN_TIMEOUT_MS = 30_000L;

    @Mock
    private SHNDeviceScannerInternal mockedSHNDeviceScannerInternal;

    @Mock
    private SHNDeviceScanner.SHNDeviceScannerListener mockedSHNDeviceScannerListener;

    @Mock
    private SHNInternalScanRequest SHNInternalScanRequestMock;

    @Captor
    ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> captureSHNDeviceScannerListener;

    private MockedHandler mockedUserHandler;
    private MockedHandler mockedInternalHandler;

    private TestSHNDeviceScanner shnDeviceScanner;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(SHNTagger.class);

        mockedUserHandler = new MockedHandler();
        mockedInternalHandler = new MockedHandler();
        mockedUserHandler.enableImmediateExecuteOnPost(false);
        mockedInternalHandler.enableImmediateExecuteOnPost(false);

        when(mockedSHNDeviceScannerInternal.startScanning(SHNInternalScanRequestMock)).thenReturn(true);
        shnDeviceScanner = new TestSHNDeviceScanner(mockedSHNDeviceScannerInternal, mockedInternalHandler.getMock(), mockedUserHandler.getMock());
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
        verify(mockedSHNDeviceScannerInternal, never()).startScanning(SHNInternalScanRequestMock);
        assertFalse(future.isDone());

        /* execute on the internal handler */
        mockedInternalHandler.executeFirstPostedExecution();
        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal).startScanning(SHNInternalScanRequestMock);
        assertTrue(future.isDone());

        /* get the result */
        assertTrue(future.get());
    }

    @Test
    public void scannerCallbacksArePostedOnTheUserHandler() throws Exception {
        FutureTask<Boolean> future = shnDeviceScanner.startScanningWithFuture(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);
        mockedInternalHandler.executeFirstPostedExecution();
        verify(mockedSHNDeviceScannerInternal).startScanning(SHNInternalScanRequestMock);

        assertEquals(0, mockedUserHandler.getPostedExecutionCount());

        SHNDeviceScanner.SHNDeviceScannerListener internalDeviceScannerListener = shnDeviceScanner.testWrappedSHNDeviceScannerListener;
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
        when(mockedSHNDeviceScannerInternal.startScanning(SHNInternalScanRequestMock)).thenReturn(false);
        FutureTask<Boolean> future1 = shnDeviceScanner.startScanningWithFuture(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);
        mockedInternalHandler.executeFirstPostedExecution();
        assertTrue(future1.isDone());
        assertFalse(future1.get());
    }

    @Test
    public void testStopScanning() throws Exception {

        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());

        /* stop scanning, before start has been called */
        shnDeviceScanner.stopScanning();

        /* verify that it is posted on the internal handler, but not executed yet */
        assertEquals(1, mockedInternalHandler.getPostedExecutionCount());

        /* verify that when executed stop is not called on the scanner, since it has not yet been started */
        mockedInternalHandler.executeFirstPostedExecution();
        verify(mockedSHNDeviceScannerInternal, never()).stopScanning(SHNInternalScanRequestMock);

        /* start scanning */
        shnDeviceScanner.startScanningWithFuture(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);
        mockedInternalHandler.executeFirstPostedExecution();

        /* stop scanning, after start has been called */
        shnDeviceScanner.stopScanning();

        /* verify that it is posted on the internal handler, but not executed yet */
        assertEquals(1, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal, never()).onStopScanning();

        /* execute on the internal handler */
        mockedInternalHandler.executeFirstPostedExecution();
        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());
        verify(mockedSHNDeviceScannerInternal).stopScanning(SHNInternalScanRequestMock);
    }

    @Test
    public void whenAnInterruptedExceptionOccursDuringScanning_thenTagIsSentWithProperData() throws Exception {

        FutureTask<Boolean> mockTask = mock(FutureTask.class);
        SHNDeviceScanner spySHNDeviceScanner = spy(shnDeviceScanner);
        doReturn(mockTask).when(spySHNDeviceScanner).startScanningWithFuture(any(SHNDeviceScanner.SHNDeviceScannerListener.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
        doThrow(new InterruptedException()).when(mockTask).get(anyLong(), any(TimeUnit.class));
        spySHNDeviceScanner.startScanning(mockedSHNDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, SCAN_TIMEOUT_MS);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Error while starting scanning: java.lang.InterruptedException", captor.getValue());
    }

    private class TestSHNDeviceScanner extends SHNDeviceScanner {
        ScannerSettingDuplicates testScannerSettingDuplicates;
        long testScanningAfterMS;
        SHNDeviceScannerListener testWrappedSHNDeviceScannerListener;

        TestSHNDeviceScanner(final SHNDeviceScannerInternal shnDeviceScannerInternal, final Handler internalHandler, final Handler userHandler) {
            super(shnDeviceScannerInternal, internalHandler, userHandler);
        }

        @NonNull
        @Override
        SHNInternalScanRequest createScanRequest(final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS, final SHNDeviceScannerListener wrappedSHNDeviceScannerListener) {
            testScannerSettingDuplicates = scannerSettingDuplicates;
            testScanningAfterMS = stopScanningAfterMS;
            testWrappedSHNDeviceScannerListener = wrappedSHNDeviceScannerListener;

            return SHNInternalScanRequestMock;
        }
    }
}
