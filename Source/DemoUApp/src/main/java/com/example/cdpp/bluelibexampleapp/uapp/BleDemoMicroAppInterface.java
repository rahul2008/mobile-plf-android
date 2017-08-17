/*
 * Copyright © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.example.cdpp.bluelibexampleapp.uapp;

import android.content.Context;
import android.content.Intent;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleActivity;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class BleDemoMicroAppInterface implements UappInterface {

    private Context context;

    private UappDependencies uappDependencies;

    private static BleDemoMicroAppInterface bleDemoMicroAppInterface;

    private SHNDevice mSelectedDevice;

    private BleDemoMicroAppInterface(){

    }

    public static BleDemoMicroAppInterface getInstance(){
        if(bleDemoMicroAppInterface==null){
            bleDemoMicroAppInterface=new BleDemoMicroAppInterface();
        }
        return bleDemoMicroAppInterface;
    }

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        this.uappDependencies=uappDependencies;
        SHNLogger.registerLogger(new SHNLogger.LogCatLogger());
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, BlueLibExampleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (uiLauncher instanceof FragmentLauncher) {
            //TODO:Need to add logic to make implmentation using fragment.
        }
    }

    public UappDependencies getUappDependencies() {
        return uappDependencies;
    }

    public void setSelectedDevice(SHNDevice device) {
        mSelectedDevice = device;
    }

    public SHNDevice getSelectedDevice() {
        return mSelectedDevice;
    }
}
