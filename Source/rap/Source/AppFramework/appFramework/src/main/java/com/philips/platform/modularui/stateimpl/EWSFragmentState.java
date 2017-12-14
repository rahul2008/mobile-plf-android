package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.ews.demoapplication.microapp.DemoUapp;
import com.philips.platform.ews.demoapplication.microapp.DemoUappDependencies;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class EWSFragmentState extends BaseState {
    public final String TAG = EWSFragmentState.class.getSimpleName();
    private Context context;

    public EWSFragmentState() {
        super(AppStates.EWS);
    }

    /**
     * to navigate
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG, " navigate to EWS Launcher called");
        DemoUappDependencies demoUappDependencies = new DemoUappDependencies((((AppFrameworkApplication) context).getAppInfra())) {
            @Override
            public CommCentral getCommCentral() {
                return ((AppFrameworkApplication) context).getCommCentralInstance();
            }
        };
        DemoUapp demoUapp = new DemoUapp();
        demoUapp.init(demoUappDependencies, new UappSettings(context));
        //its up to proposition to pass theme or not, if not passing theme then it will show default theme of library
        demoUapp.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, new ThemeConfiguration(context, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, AccentRange.ORANGE, NavigationColor.BRIGHT), -1, null),
                (null));
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }

}
