/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.app.Application;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.utility.SHNLogger;

public class TestApplication extends Application {

    private SHNCentral shnCentral;
    private SHNDevice selectedDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        SHNLogger.registerLogger(new SHNLogger.LogCatLogger());
    }

    public void setShnCentral(SHNCentral shnCentral) {
        this.shnCentral = shnCentral;
    }

    public SHNCentral getShnCentral() {
        return shnCentral;
    }

    public SHNDevice getSelectedDevice() {
        return selectedDevice;
    }

    public void setSelectedDevice(SHNDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
    }
}

