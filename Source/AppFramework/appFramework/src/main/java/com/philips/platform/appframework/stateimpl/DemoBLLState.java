/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppInterface;
import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppSettings;
import com.example.cdpp.bluelibexampleapp.uapp.DefaultBleDemoMicroAppDependencies;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DemoBLLState extends DemoBaseState {
    private Context context;

    public DemoBLLState() {
        super(AppStates.TESTBLUELIB);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        BleDemoMicroAppInterface uAppInterface = getBleUApp();
        uAppInterface.init(getUappDependencies(),
                getUappSettings());// pass App-infra instance instead of null
        uAppInterface.launch(getUiLauncher(), null);
    }

    @NonNull
    protected ActivityLauncher getUiLauncher() {
        return new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null);
    }

    @NonNull
    protected BleDemoMicroAppSettings getUappSettings() {
        return new BleDemoMicroAppSettings(context);
    }

    @NonNull
    protected DefaultBleDemoMicroAppDependencies getUappDependencies() {
        return new DefaultBleDemoMicroAppDependencies(context);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    protected BleDemoMicroAppInterface getBleUApp() {
        return BleDemoMicroAppInterface.getInstance();
    }

    @Override
    public void updateDataModel() {

    }
}
