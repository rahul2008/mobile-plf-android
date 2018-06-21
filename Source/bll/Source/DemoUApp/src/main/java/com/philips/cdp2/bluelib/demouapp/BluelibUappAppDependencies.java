/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.bluelib.demouapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.pluginreferenceboard.DeviceDefinitionInfoReferenceBoard;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.tagging.AppInfraTagger;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import static android.content.ContentValues.TAG;

public class BluelibUappAppDependencies extends UappDependencies {

    private SHNCentral shnCentral;

    public BluelibUappAppDependencies(final @NonNull Context context, final @NonNull AppInfraInterface appInfraInstance) {
        super(null);

        SHNTagger.registerTagger(new AppInfraTagger(appInfraInstance));
        SHNLogger.registerLogger(new SHNLogger.LogCatLogger());

        SHNCentral.Builder builder = new SHNCentral.Builder(context);
        builder.showPopupIfBLEIsTurnedOff(true);

        try {
            shnCentral = builder.create();

            SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = new DeviceDefinitionInfoReferenceBoard();
            shnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);
        } catch (SHNBluetoothHardwareUnavailableException e) {
            SHNLogger.e(TAG, "Error obtaining BlueLib instance: " + e.getMessage());
        }
    }

    public SHNCentral getShnCentral() {
        return shnCentral;
    }
}
