package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.prdemoapp.PRDemoAppuAppDependencies;
import com.philips.platform.prdemoapp.PRDemoAppuAppInterface;
import com.philips.platform.prdemoapp.PRDemoAppuAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoPRState extends BaseState {

    private Context context;

    public DemoPRState() {
        super(AppStates.TESTPR);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        PRDemoAppuAppInterface uAppInterface = new PRDemoAppuAppInterface();
        uAppInterface.init(new PRDemoAppuAppDependencies(((AppFrameworkApplication) context.getApplicationContext()).getAppInfra()), new PRDemoAppuAppSettings(context.getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);// pass launch input if required

    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void updateDataModel() {

    }
}
