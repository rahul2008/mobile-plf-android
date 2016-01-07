/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScanner {
    private final SHNDeviceScannerInternal shnDeviceScannerInternal;
    private final Handler userHandler;

    public enum ScannerSettingDuplicates {
        DuplicatesNotAllowed, DuplicatesAllowed
    }

    public interface SHNDeviceScannerListener {
        void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo);
        void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    /* package */ SHNDeviceScanner(SHNDeviceScannerInternal shnDeviceScannerInternal, Handler userHandler) {
        this.shnDeviceScannerInternal = shnDeviceScannerInternal;
        this.userHandler = userHandler;
    }

    public boolean startScanning(final SHNDeviceScannerListener shnDeviceScannerListener, ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        SHNDeviceScannerListener wrappedSHNDeviceScannerListener = new SHNDeviceScannerListener() {
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
        return shnDeviceScannerInternal.startScanning(wrappedSHNDeviceScannerListener, scannerSettingDuplicates, stopScanningAfterMS);
    }

    public void stopScanning() {
        shnDeviceScannerInternal.stopScanning();
    }
}
