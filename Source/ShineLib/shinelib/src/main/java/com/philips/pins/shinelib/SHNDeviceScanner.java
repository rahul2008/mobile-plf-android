/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScanner {
    private final SHNDeviceScannerInternal shnDeviceScannerInternal;
    private final Handler userHandler;
    private final Handler internalHandler;

    public enum ScannerSettingDuplicates {
        DuplicatesNotAllowed, DuplicatesAllowed
    }

    public interface SHNDeviceScannerListener {
        void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo);
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
            public void deviceFound(SHNDeviceScanner shnDeviceScanner, final SHNDeviceFoundInfo shnDeviceFoundInfo) {
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
                return shnDeviceScannerInternal.startScanning(wrappedSHNDeviceScannerListener, scannerSettingDuplicates, stopScanningAfterMS);
            }
        };
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(booleanCallable);

        internalHandler.post(futureTask);

        return futureTask;
    }

    public boolean startScanning(final SHNDeviceScannerListener shnDeviceScannerListener, final ScannerSettingDuplicates scannerSettingDuplicates, final long stopScanningAfterMS) {
        FutureTask<Boolean> futureTask = startScanningWithFuture(shnDeviceScannerListener, scannerSettingDuplicates, stopScanningAfterMS);

        boolean result = false;
        try {
            result = futureTask.get().booleanValue();
        } catch (InterruptedException e) {
            assert(e == null); // Should not occur ever...
        } catch (ExecutionException e) {
            assert(e == null); // Should not occur ever...
        }
        return result;
    }

    public void stopScanning() {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                shnDeviceScannerInternal.stopScanning();
            }
        });
    }

    public SHNDeviceScannerInternal getShnDeviceScannerInternal() {
        return shnDeviceScannerInternal;
    }
}
