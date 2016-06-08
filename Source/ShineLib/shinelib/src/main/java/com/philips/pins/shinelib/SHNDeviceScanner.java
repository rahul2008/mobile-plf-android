/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class that allows the API users to discover peripherals.
 * <p/>
 * Note that only peripherals serviced by a plugin that is registered with {@link SHNCentral} are exposed by {@code SHNDeviceScanner}.
 * The matching behaviour of the scanner is under control of the {@link SHNDeviceDefinitionInfo#useAdvertisedDataMatcher()} method in the corresponding
 * peripheral's device definition info. {@code SHNDeviceScanner} can be obtained via {@link SHNCentral#getShnDeviceScanner()}.
 */
public class SHNDeviceScanner {
    private static final String TAG = "SHNDeviceScanner";

    private final SHNDeviceScannerInternal shnDeviceScannerInternal;
    private final Handler userHandler;
    private final Handler internalHandler;

    private SHNInternalScanRequest shnInternalScanRequest;

    /**
     * Possible scanning settings. Indicates if devices may be reported more than once.
     */
    public enum ScannerSettingDuplicates {
        /**
         *  Indicates that devices may not be reported more than once.
         */
        DuplicatesNotAllowed,
        /**
         *  Indicates that devices could be reported more than once.
         */
        DuplicatesAllowed
    }

    /**
     * Interface that provides updates for a scanning result.
     */
    public interface SHNDeviceScannerListener {
        /**
         * This method is called to indicate that a peripheral was found.
         *
         * @param shnDeviceScanner   used for scanning
         * @param shnDeviceFoundInfo of the found peripheral
         */
        void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo);

        /**
         * This method is called to indicate that the scan has been stopped.
         *
         * @param shnDeviceScanner used for scanning
         */
        void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    /* package */ SHNDeviceScanner(SHNDeviceScannerInternal shnDeviceScannerInternal, Handler internalHandler, Handler userHandler) {
        this.shnDeviceScannerInternal = shnDeviceScannerInternal;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @VisibleForTesting
    /* package */  FutureTask<Boolean> startScanningWithFuture(final SHNDeviceScannerListener shnDeviceScannerListener, final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS) {
        final SHNDeviceScannerListener wrappedSHNDeviceScannerListener = new SHNDeviceScannerListener() {
            @Override
            public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull final SHNDeviceFoundInfo shnDeviceFoundInfo) {
                userHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (shnDeviceScannerListener != null) {
                            shnDeviceScannerListener.deviceFound(SHNDeviceScanner.this, shnDeviceFoundInfo);
                        }
                    }
                });
            }

            @Override
            public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
                userHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (shnDeviceScannerListener != null) {
                            shnDeviceScannerListener.scanStopped(SHNDeviceScanner.this);
                        }
                    }
                });
            }
        };

        Callable<Boolean> booleanCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                stopScanningInternal();
                shnInternalScanRequest = createScanRequest(scannerSettingDuplicates, stopScanningAfterMS, wrappedSHNDeviceScannerListener);
                return shnDeviceScannerInternal.startScanning(shnInternalScanRequest);
            }
        };
        FutureTask<Boolean> futureTask = new FutureTask<>(booleanCallable);

        internalHandler.post(futureTask);

        return futureTask;
    }

    @NonNull
    /* package */ SHNInternalScanRequest createScanRequest(final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS, final SHNDeviceScannerListener wrappedSHNDeviceScannerListener) {
        return new SHNInternalScanRequest(null, null, scannerSettingDuplicates == ScannerSettingDuplicates.DuplicatesAllowed, stopScanningAfterMS, wrappedSHNDeviceScannerListener);
    }

    /**
     * Starts a new scan with the duplication option and the timeout interval in milliseconds. If a previously started scan is still running, it will be stopped.
     *
     * @param shnDeviceScannerListener an instance of a listener to receive scanning callbacks
     * @param scannerSettingDuplicates specified duplication option
     * @param stopScanningAfterMS timeout interval in milliseconds
     * @return true if scan was started successfully, false otherwise
     */
    public boolean startScanning(final SHNDeviceScannerListener shnDeviceScannerListener, final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS) {
        FutureTask<Boolean> futureTask = startScanningWithFuture(shnDeviceScannerListener, scannerSettingDuplicates, stopScanningAfterMS);

        boolean result = false;
        try {
            result = futureTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            SHNLogger.e(TAG, "startScanning InterruptedException", e);
            assert (e == null); // Should not occur ever...
        } catch (ExecutionException e) {
            SHNLogger.e(TAG, "startScanning ExecutionException", e);
            assert (e == null); // Should not occur ever...
        } catch (TimeoutException e) {
            SHNLogger.e(TAG, "startScanning TimeoutException", e);
            assert (e == null); // Should not occur ever...
        }
        return result;
    }

    /**
     * Stops any currently running scan.
     */
    public void stopScanning() {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                stopScanningInternal();
            }
        });
    }

    private void stopScanningInternal() {
        if (shnInternalScanRequest != null) {
            shnDeviceScannerInternal.stopScanning(shnInternalScanRequest);
            shnInternalScanRequest = null;
        }
    }

    /**
     * Get a scanner that creates requests and handles responses on the internal BlueLib thread. Do not use in an app.
     *
     * @return an instance of an internal scanner.
     */
    public SHNDeviceScannerInternal getShnDeviceScannerInternal() {
        return shnDeviceScannerInternal;
    }
}
