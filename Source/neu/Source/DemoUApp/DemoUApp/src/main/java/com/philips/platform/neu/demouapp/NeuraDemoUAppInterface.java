/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.neu.demouapp;


import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class NeuraDemoUAppInterface implements UappInterface {

    private Context mContext;
    private static AppInfraInterface appInfra;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.mContext = uappSettings.getContext();
        appInfra = uappDependencies.getAppInfra();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(((ActivityLauncher) uiLauncher).getActivityContext(), NueraDemoMainActivity.class);
            mContext.startActivity(intent);
        }

    }

    public static AppInfraInterface getAppInfra() {
        return appInfra;
    }

}
