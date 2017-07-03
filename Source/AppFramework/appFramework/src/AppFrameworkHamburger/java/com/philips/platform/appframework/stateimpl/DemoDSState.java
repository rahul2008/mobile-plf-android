package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoDSState extends BaseState {

    private Context context;
    public DemoDSState() {
        super(AppStates.TESTDATASERVICE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        int DEFAULT_THEME = R.style.Theme_Philips_DarkBlue_WhiteBackground;
        DSDemoAppuAppSettings dsDemoAppuAppSettings = new DSDemoAppuAppSettings(context.getApplicationContext());

        DSDemoAppuAppInterface dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
        dsDemoAppuAppInterface.init(new DSDemoAppuAppDependencies(((AppFrameworkApplication)context.getApplicationContext()).getAppInfra()), dsDemoAppuAppSettings);
        dsDemoAppuAppInterface.launch(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, DEFAULT_THEME), null);
    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
