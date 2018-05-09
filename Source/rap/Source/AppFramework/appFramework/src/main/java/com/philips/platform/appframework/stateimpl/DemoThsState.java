/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.myapplication.DemoMicroAppApplicationuAppDependencies;
import com.philips.platform.myapplication.DemoMicroAppApplicationuAppInterface;
import com.philips.platform.myapplication.DemoMicroAppApplicationuAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class DemoThsState extends DemoBaseState {

    private Context appContext;
    DemoMicroAppApplicationuAppInterface mDemoMicroAppApplicationuAppInterface;

    public DemoThsState() {
        super(AppStates.TELEHEALTHSERVICESDEMO);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        final DemoMicroAppApplicationuAppDependencies uappDependencies = new DemoMicroAppApplicationuAppDependencies(((AppFrameworkApplication)appContext.getApplicationContext()).getAppInfra());
        ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT,new ThemeConfiguration(appContext, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE), 0, null);

        DemoMicroAppApplicationuAppInterface uAppInterface = getDemoMicroAppApplicationuAppInterface();
        uAppInterface.init(uappDependencies, new DemoMicroAppApplicationuAppSettings(appContext));// pass App-infra instance instead of null
        uAppInterface.launch(activityLauncher, null);// pass launch input if required
    }

    @NonNull
    private DemoMicroAppApplicationuAppInterface getDemoMicroAppApplicationuAppInterface() {
        if(mDemoMicroAppApplicationuAppInterface == null) {
            mDemoMicroAppApplicationuAppInterface = new DemoMicroAppApplicationuAppInterface();
        }
        return mDemoMicroAppApplicationuAppInterface;
    }

    @Override
    public void init(Context context) {
        appContext = context;
    }

    @Override
    public void updateDataModel(){

    }

}
