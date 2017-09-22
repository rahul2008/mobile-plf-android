/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppInterface;
import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppSettings;
import com.example.cdpp.bluelibexampleapp.uapp.DefaultBleDemoMicroAppDependencies;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class DemoBLLState extends DemoBaseState {
    private Context context;

    public DemoBLLState() {
        super(AppStates.TESTBLUELIB);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        BleDemoMicroAppInterface uAppInterface = BleDemoMicroAppInterface.getInstance();
        uAppInterface.init(new DefaultBleDemoMicroAppDependencies(context.getApplicationContext()), new BleDemoMicroAppSettings(getApplicationContext()));// pass App-infra instance instead of null

        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null), null);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }
}
