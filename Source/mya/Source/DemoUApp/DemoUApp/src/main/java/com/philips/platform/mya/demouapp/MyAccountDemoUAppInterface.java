/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.demouapp;


import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class MyAccountDemoUAppInterface implements UappInterface {

    private static User user;
    private Context mContext;
    private static AppInfraInterface appInfra;
    private static UserDataInterface userDataInterface;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        mContext = uappSettings.getContext();
        appInfra = uappDependencies.getAppInfra();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        MyaDemouAppLaunchInput myaDemouAppLaunchInput = (MyaDemouAppLaunchInput) uappLaunchInput;
        MyAccountDemoUAppInterface.user = myaDemouAppLaunchInput.getUser();
        MyAccountDemoUAppInterface.userDataInterface = myaDemouAppLaunchInput.getUserDataInterface();
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity();
        }
    }

    private void launchAsActivity() {
        Intent intent = new Intent(mContext, DemoAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public static UserDataInterface getUserDataInterface() {
        return userDataInterface;
    }

    public static User getUserObject() {
        return user;
    }
}
