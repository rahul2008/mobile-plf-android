/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import androidx.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.ccdemouapp.CCDemoUAppuAppDependencies;
import com.philips.platform.ccdemouapp.CCDemoUAppuAppInterface;
import com.philips.platform.ccdemouapp.CCDemoUAppuAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoDCCState extends DemoBaseState {

    private Context context;

    public DemoDCCState() {
        super(AppStates.TESTCC);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        CCDemoUAppuAppInterface uAppInterface = getCcDemoUAppuAppInterface();
        uAppInterface.init(new CCDemoUAppuAppDependencies(((AppFrameworkApplication)context.getApplicationContext()).getAppInfra()), new CCDemoUAppuAppSettings(context.getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(context, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null), null);

    }

    @NonNull
    protected CCDemoUAppuAppInterface getCcDemoUAppuAppInterface() {
        return new CCDemoUAppuAppInterface();
    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
