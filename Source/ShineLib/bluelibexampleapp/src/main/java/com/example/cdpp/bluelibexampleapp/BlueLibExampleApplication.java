/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

import com.philips.cdp.pluginreferenceboard.DeviceDefinitionInfoReferenceBoard;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinepluginmoonshinelib.SHNMoonshineDeviceDefinitionInfo;

public class BlueLibExampleApplication extends Application {

    private static final String TAG = "BlueLibExampleApplication";

    private static BlueLibExampleApplication sApplication;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private SHNCentral mShnCentral;

    private SHNDevice mSelectedDevice;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        // Setup logger
        SHNLogger.registerLogger(new SHNLogger.LogCatLogger());

        // BlueLib handler thread
        mHandlerThread = new HandlerThread("BlueLibThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        // Obtain BlueLib instance
        SHNCentral.Builder builder = new SHNCentral.Builder(this);
        builder.showPopupIfBLEIsTurnedOff(true);
        builder.setHandler(mHandler);

        try {
            mShnCentral = builder.create();
        } catch (SHNBluetoothHardwareUnavailableException e) {
            SHNLogger.e(TAG, "Error obtaining BlueLib instance: " + e.getMessage());

            mHandlerThread.quitSafely();
            mHandler = null;
        }

        setupDeviceDefinitions();
    }

    public static final BlueLibExampleApplication get() {
        if (sApplication == null) {
            throw new RuntimeException("Application not initialized yet.");
        }
        return sApplication;
    }

    public final SHNCentral getShnCentral() {
        return mShnCentral;
    }

    private final void setupDeviceDefinitions() {
        SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = new DeviceDefinitionInfoReferenceBoard();
        mShnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);

        SHNMoonshineDeviceDefinitionInfo shnMoonshineDeviceDefinitionInfo = new SHNMoonshineDeviceDefinitionInfo();
        mShnCentral.registerDeviceDefinition(shnMoonshineDeviceDefinitionInfo);
    }

    public void setSelectedDevice(SHNDevice device) {
        mSelectedDevice = device;
    }

    public SHNDevice getSelectedDevice() {
        return mSelectedDevice;
    }
}
