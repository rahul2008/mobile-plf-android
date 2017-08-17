package com.example.cdpp.bluelibexampleapp.uapp;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.cdpp.bluelibexampleapp.device.DeviceScanner;
import com.philips.cdp.pluginreferenceboard.DeviceDefinitionInfoReferenceBoard;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;

/**
 * Created by philips on 14/08/17.
 */

public class DefaultBleDemoMicroAppDependencies extends BleDemoMicroAppDependencies {


    private DeviceScanner mDeviceScanner;


    public DefaultBleDemoMicroAppDependencies(final @NonNull Context context) {
        super();
        SHNCentral.Builder builder = new SHNCentral.Builder(context);
        builder.showPopupIfBLEIsTurnedOff(true);

        try {
            shnCentral = builder.create();
        } catch (SHNBluetoothHardwareUnavailableException e) {
            SHNLogger.e(TAG, "Error obtaining BlueLib instance: " + e.getMessage());
        }

        // Create device scanner
        mDeviceScanner = new DeviceScanner(shnCentral, new Handler(getMainLooper()));
        SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = new DeviceDefinitionInfoReferenceBoard();
        shnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);
    }

}
