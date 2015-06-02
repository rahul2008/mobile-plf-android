package com.pins.philips.shinelib.associationprocedures;

import android.util.Log;

import com.pins.philips.shinelib.SHNAssociationProcedure;
import com.pins.philips.shinelib.SHNDevice;
import com.pins.philips.shinelib.SHNDeviceFoundInfo;
import com.pins.philips.shinelib.framework.Timer;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by code1_310170470 on 28/05/15.
 */
public class SHNAssociationProcedureNearestDevice implements SHNAssociationProcedure {
    public static final long NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS      = 10000L;
    public static final int ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT = 3;
    public static final int NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT    = 5;

    private static final String TAG = SHNAssociationProcedureNearestDevice.class.getSimpleName();
    private static final boolean LOGGING = false;
    private SHNAssociationProcedureListener listener;
    private SortedMap<Integer, SHNDevice> discoveredDevices;
    private Timer nearestDeviceIterationTimer;
    private int nearestDeviceIterationCount;
    private int successivelyNearestDeviceCount;
    private SHNDevice nearestDeviceInPreviousIteration;

    public SHNAssociationProcedureNearestDevice(SHNAssociationProcedure.SHNAssociationProcedureListener shnAssociationProcedureListener)
    {
        listener = shnAssociationProcedureListener;
        discoveredDevices = new TreeMap<>();
        nearestDeviceIterationCount = 0;
        successivelyNearestDeviceCount = 0;
        nearestDeviceIterationTimer = Timer.createTimer(new Runnable() {
            @Override
            public void run() {
                associateWithNearestDeviceIfPossible();
            }
        }, NEAREST_DEVICE_ITERATION_TIME_IN_MILLI_SECONDS);
        nearestDeviceIterationTimer.restart();
    }


    private void associateWithNearestDeviceIfPossible() {
        nearestDeviceIterationCount++;
        SHNDevice nearestDevice = discoveredDevices.isEmpty() ? null : discoveredDevices.get(discoveredDevices.lastKey());
        if (LOGGING) Log.i(TAG, String.format("[ %d ] Nearest device: '%s'", nearestDeviceIterationCount, (nearestDevice != null) ? nearestDevice.getAddress() : "NONE"));
        discoveredDevices.clear();
        boolean finished = false;

        if ((nearestDevice != null) && (nearestDeviceInPreviousIteration != null) && (nearestDevice.getAddress().equals(nearestDeviceInPreviousIteration.getAddress()))) {
            if (LOGGING) Log.i(TAG, "associateWithNearestDeviceIfPossible address matched with previous iteration");
            if (++successivelyNearestDeviceCount == ASSOCIATE_WHEN_DEVICE_IS_SUCCESSIVELY_NEAREST_COUNT) {
                nearestDeviceInPreviousIteration = null;
                listener.onStopScanRequest();
                listener.onAssociationSuccess(nearestDevice);
                finished = true;
            }
        } else {
            if (LOGGING) Log.i(TAG, "associateWithNearestDeviceIfPossible address NOT matched with previous iteration");
            nearestDeviceInPreviousIteration = nearestDevice;
            successivelyNearestDeviceCount = 1;
        }

        if (!finished) {
            startNextIterationOrFail();
        }
    }

    private void startNextIterationOrFail() {
        if (nearestDeviceIterationCount < NEAREST_DEVICE_DETERMINATION_MAX_ITERATION_COUNT) {
            nearestDeviceIterationTimer.restart();
        } else {
            if (LOGGING) Log.i(TAG, "!! No device consistently deemed nearest; association failed");
            listener.onAssociationFailed(null);
        }
    }

    // implements SHNAssociationProcedure
    @Override
    public boolean getShouldScan() {
        return true;
    }

    @Override
    public void deviceDiscovered(SHNDevice shnDevice, SHNDeviceFoundInfo shnDeviceFoundInfo) {
        if (LOGGING) Log.i(TAG, String.format("deviceDiscovered '%s'; rssi = %d", shnDevice.getAddress(), shnDeviceFoundInfo.getRssi()));
        if (shnDeviceFoundInfo.getRssi() != 0) {
            discoveredDevices.put(shnDeviceFoundInfo.getRssi(), shnDevice);
        } else {
            if (LOGGING) Log.i(TAG, String.format("Ignoring discovered device '%s'; rssi = 0", shnDevice.toString()));
        }
    }

    @Override
    public void scannerTimeout() {
        nearestDeviceIterationTimer.stop();
        listener.onAssociationFailed(null);
    }
}
