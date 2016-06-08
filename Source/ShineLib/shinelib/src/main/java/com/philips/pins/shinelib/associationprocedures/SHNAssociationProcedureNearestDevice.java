/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
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
 * A procedure that can be used to associate with a peripheral. The procedure performs scanning for a peripheral 5 times. If the peripheral is the nearest according to RSSI 3 times in a row, then
 * the peripheral is chosen and reported via {@link com.philips.pins.shinelib.SHNAssociationProcedurePlugin.SHNAssociationProcedureListener#onAssociationSuccess(SHNDevice)} callback.
 */
public class SHNAssociationProcedureNearestDevice implements SHNAssociationProcedurePlugin {

    /**
     * Timeout for a scanning iteration.
     */
    public static final long NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS = 10000L;
    /**
     * Number of times in a row the peripheral is required to be nearest to be selected as the associated {@code SHNDevice}
     */
    public static final int ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT = 3;
    /**
     * Maximum number of the scanning iterations.
     */
    public static final int NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT = 5;

    private static final String TAG = SHNAssociationProcedureNearestDevice.class.getSimpleName();
    private SHNAssociationProcedureListener shnAssociationProcedureListener;
    private SortedMap<Integer, SHNDevice> discoveredDevices;
    private Timer nearestDeviceIterationTimer;
    private int nearestDeviceIterationCount;
    private int successivelyNearestDeviceCount;
    private SHNDevice nearestDeviceInPreviousIteration;

    public SHNAssociationProcedureNearestDevice(SHNAssociationProcedureListener shnAssociationProcedureListener) {
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

            successivelyNearestDeviceCount++;
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

    /**
     * Checks if the peripheral can be chosen as the nearest.
     *
     * @param successivelyNearestDeviceCount number of times in a row the peripheral is discovered as nearest
     * @return true if the successivelyNearestDeviceCount number equals ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT
     */
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

    /**
     * Returns a {@link Timer} for the specified runnable.
     *
     * @param runnable that is Timer created for
     * @return Timer instance with NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS time out
     */
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
