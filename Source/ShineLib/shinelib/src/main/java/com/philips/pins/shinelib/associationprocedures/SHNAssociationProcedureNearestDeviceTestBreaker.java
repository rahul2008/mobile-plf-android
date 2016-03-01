/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.associationprocedures;



import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNAssociationProcedurePlugin;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by code1_310170470 on 28/05/15.
 */
public class SHNAssociationProcedureNearestDeviceTestBreaker implements SHNAssociationProcedurePlugin {
    public static final long NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS = 10000L;
    public static final int ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT = 3;
    public static final int NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT = 5;

    private static final String TAG = SHNAssociationProcedureNearestDeviceTestBreaker.class.getSimpleName();
    private SHNAssociationProcedureListener shnAssociationProcedureListener;
    private SortedMap<Integer, SHNDevice> discoveredDevices;
    private Timer nearestDeviceIterationTimer;
    private int nearestDeviceIterationCount;
    private int successivelyNearestDeviceCount;
    private SHNDevice nearestDeviceInPreviousIteration;

    public SHNAssociationProcedureNearestDeviceTestBreaker(SHNAssociationProcedureListener shnAssociationProcedureListener) {
        this.shnAssociationProcedureListener = shnAssociationProcedureListener;
    }

    private void associateWithNearestDeviceIfPossible() {
        nearestDeviceIterationCount++;
        SHNDevice nearestDevice = discoveredDevices.isEmpty() ? null : discoveredDevices.get(discoveredDevices.lastKey());
        SHNLogger.i(TAG, String.format("[ %d ] Nearest device: '%s'", nearestDeviceIterationCount, (nearestDevice != null) ? nearestDevice.getAddress() : "NONE"));
        discoveredDevices.clear();
        boolean finished = false;

        if ((nearestDevice != null) &&
                        (((nearestDeviceInPreviousIteration != null) && (nearestDevice.getAddress().equals(nearestDeviceInPreviousIteration.getAddress())))
                        ||
                        (successivelyNearestDeviceCount == 0 && deviceIsSufficientlyOftenNearest(1)))) {
            SHNLogger.i(TAG, "associateWithNearestDeviceIfPossible address matched with previous iteration");

            ++successivelyNearestDeviceCount;
            if (deviceIsSufficientlyOftenNearest(successivelyNearestDeviceCount)) {
                nearestDeviceInPreviousIteration = null;
                if (shnAssociationProcedureListener != null) {
                    shnAssociationProcedureListener.onStopScanRequest();
                    shnAssociationProcedureListener.onAssociationSuccess(nearestDevice);
                }
                finished = true;
            }
        } else {
            SHNLogger.i(TAG, "associateWithNearestDeviceIfPossible address NOT matched with previous iteration");
            nearestDeviceInPreviousIteration = nearestDevice;
            successivelyNearestDeviceCount = 1;
        }

        if (!finished) {
            startNextIterationOrFail();
        }
    }

    protected boolean deviceIsSufficientlyOftenNearest(int successivelyNearestDeviceCount) {
        return successivelyNearestDeviceCount == ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT;
    }

    private void startNextIterationOrFail() {
        if (nearestDeviceIterationCount < NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT) {
            nearestDeviceIterationTimer.restart();
        } else {
            SHNLogger.i(TAG, "!! No device consistently deemed nearest; association failed");
            if (shnAssociationProcedureListener != null) {
                shnAssociationProcedureListener.onAssociationFailed(null, SHNResult.SHNErrorAssociationFailed);
            }
        }
    }

    @Override
    public SHNResult start() {
        discoveredDevices = new TreeMap<>();
        nearestDeviceIterationCount = 0;
        successivelyNearestDeviceCount = 0;
        nearestDeviceIterationTimer = createTimerForRunnable(new Runnable() {
            @Override
            public void run() {
                associateWithNearestDeviceIfPossible();
            }
        });
        nearestDeviceIterationTimer.restart();
        return SHNResult.SHNOk;
    }

    @NonNull
    protected Timer createTimerForRunnable(Runnable runnable) {
        return Timer.createTimer(runnable, NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS);
    }

    @Override
    public void stop() {
        nearestDeviceIterationTimer.stop();
    }

    // implements SHNAssociationProcedure
    @Override
    public boolean getShouldScan() {
        return true;
    }

    @Override
    public void deviceDiscovered(SHNDevice shnDevice, SHNDeviceFoundInfo shnDeviceFoundInfo) {
        SHNLogger.i(TAG, String.format("deviceDiscovered '%s'; rssi = %d", shnDevice.getAddress(), shnDeviceFoundInfo.getRssi()));
        if (shnDeviceFoundInfo.getRssi() != 0) {
            discoveredDevices.put(shnDeviceFoundInfo.getRssi(), shnDevice);
        } else {
            SHNLogger.i(TAG, String.format("Ignoring discovered device '%s'; rssi = 0", shnDevice.toString()));
        }
    }

    @Override
    public void scannerTimeout() {
        nearestDeviceIterationTimer.stop();
        if (shnAssociationProcedureListener != null) {
            shnAssociationProcedureListener.onAssociationFailed(null, SHNResult.SHNErrorTimeout);
        }
    }

    @Override
    public void setShnAssociationProcedureListener(SHNAssociationProcedureListener shnAssociationProcedureListener) {
        this.shnAssociationProcedureListener = shnAssociationProcedureListener;
    }
}
