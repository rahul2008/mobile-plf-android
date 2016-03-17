/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScanner {
    private static final String TAG = "SHNDeviceScanner";

    private final SHNDeviceScannerInternal shnDeviceScannerInternal;
    private final Handler userHandler;
    private final Handler internalHandler;

    private SHNInternalScanRequest shnInternalScanRequest;

    public enum ScannerSettingDuplicates {
        DuplicatesNotAllowed, DuplicatesAllowed
    }

    public interface SHNDeviceScannerListener {
        void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo);

        void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    /* package */ SHNDeviceScanner(SHNDeviceScannerInternal shnDeviceScannerInternal, Handler internalHandler, Handler userHandler) {
        this.shnDeviceScannerInternal = shnDeviceScannerInternal;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    protected FutureTask<Boolean> startScanningWithFuture(final SHNDeviceScannerListener shnDeviceScannerListener, final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS) {
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
    SHNInternalScanRequest createScanRequest(final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS, final SHNDeviceScannerListener wrappedSHNDeviceScannerListener) {
        return new SHNInternalScanRequest(null, null, scannerSettingDuplicates == ScannerSettingDuplicates.DuplicatesAllowed, stopScanningAfterMS, wrappedSHNDeviceScannerListener);
    }

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

    public SHNDeviceScannerInternal getShnDeviceScannerInternal() {
        return shnDeviceScannerInternal;
    }
}
