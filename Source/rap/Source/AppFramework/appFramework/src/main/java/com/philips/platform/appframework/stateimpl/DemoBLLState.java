/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.BluelibUappAppDependencies;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class DemoBLLState extends DemoBaseState {
    private Context context;

    public DemoBLLState() {
        super(AppStates.TESTBLUELIB);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        UappInterface uAppInterface = getBleUApp();
        uAppInterface.init(getUappDependencies(), getUappSettings());
        uAppInterface.launch(getUiLauncher(), null);
    }

    @NonNull
    protected ActivityLauncher getUiLauncher() {
        return new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null);
    }

    @NonNull
    protected UappSettings getUappSettings() {
        return new UappSettings(context);
    }

    @NonNull
    protected UappDependencies getUappDependencies() {
        return new BluelibUappAppDependencies(context);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    protected UappInterface getBleUApp() {
        return BluelibUapp.get();
    }

    @Override
    public void updateDataModel() {

    }
}
