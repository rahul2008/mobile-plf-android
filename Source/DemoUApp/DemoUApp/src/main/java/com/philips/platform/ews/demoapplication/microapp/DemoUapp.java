/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.demoapplication.microapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.philips.platform.ews.demoapplication.EWSDemoUActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/*
 * EWSInterface is an entry point for EWS launching,
 * All the initialisation for EWS should be done using it.
 */
@SuppressWarnings("WeakerAccess")
public class DemoUapp implements UappInterface {

    public static final String ERROR_MSG_INVALID_CALL = "Please call \"init\" method, before calling launching ews with valid params";
    public static final String ERROR_MSG_INVALID_IMPLEMENTATION = "Please implement EWSActionBarListener in Activity";
    public static final String SCREEN_ORIENTATION = "screen.orientation";
    public static final String PRODUCT_NAME = "productName";
    private static final String TAG = "EWSInterface";
    @NonNull
    private Context context;

    private UAppDependencyHelper uAppDependencyHelper;

    /**
     * Entry point for EWS. Please make sure no EWS components are being used before EWSInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application context.
     */
    @Override
    public void init(@NonNull final UappDependencies uappDependencies, @NonNull final UappSettings uappSettings) {
        DemoUappDependencies demoUappDependencies = (DemoUappDependencies) uappDependencies;
        uAppDependencyHelper = new UAppDependencyHelper(uappDependencies.getAppInfra(), demoUappDependencies.getCommCentral());
        context = uappSettings.getContext();
    }

    /**
     * Launches the EWS user interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - URLaunchInput
     */
    @Override
    public void launch(@NonNull final UiLauncher uiLauncher, @NonNull final UappLaunchInput uappLaunchInput) {
        uAppDependencyHelper.setThemeConfiguration(((ActivityLauncher) uiLauncher).getDlsThemeConfiguration());
        launchAsActivity(uappLaunchInput);
    }

    private void launchAsActivity(@NonNull final UappLaunchInput uappLaunchInput) {
        Intent intent = new Intent(context, EWSDemoUActivity.class);
        intent.putExtra(SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

}