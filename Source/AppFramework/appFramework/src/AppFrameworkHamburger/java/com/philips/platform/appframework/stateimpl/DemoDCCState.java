package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.ccdemouapp.CCDemoUAppuAppDependencies;
import com.philips.platform.ccdemouapp.CCDemoUAppuAppInterface;
import com.philips.platform.ccdemouapp.CCDemoUAppuAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoDCCState extends BaseState {

    private Context context;

    public DemoDCCState() {
        super(AppStates.TESTCC);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        CCDemoUAppuAppInterface uAppInterface = new CCDemoUAppuAppInterface();
        uAppInterface.init(new CCDemoUAppuAppDependencies(((AppFrameworkApplication)context.getApplicationContext()).getAppInfra()), new CCDemoUAppuAppSettings(context.getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);

    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
