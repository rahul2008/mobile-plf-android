package com.philips.cdp2.commlib;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BleDeviceCache implements SHNDeviceScanner.SHNDeviceScannerListener {
    private final Map<String, SHNDevice> deviceMap = new HashMap<>();


    @VisibleForTesting
    String getDeviceCppId(SHNDevice device) {
        //TODO implement

        return device.getAddress(); // WRONG!!!
    }

    @NonNull
    public Map<String, SHNDevice> getDeviceMap(){
        return Collections.unmodifiableMap(deviceMap);
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();
        deviceMap.put(getDeviceCppId(device), device);
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        //don't care
    }
}
