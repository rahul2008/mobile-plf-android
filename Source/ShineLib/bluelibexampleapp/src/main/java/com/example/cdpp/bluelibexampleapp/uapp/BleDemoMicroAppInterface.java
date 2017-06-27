/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.example.cdpp.bluelibexampleapp.uapp;

import android.content.Context;
import android.content.Intent;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class BleDemoMicroAppInterface implements UappInterface {
    private Context context;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
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
}
