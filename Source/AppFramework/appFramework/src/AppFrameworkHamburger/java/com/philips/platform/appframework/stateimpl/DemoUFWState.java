package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappdemo.UappDemoDependencies;
import com.philips.platform.uappdemo.UappDemoInterface;
import com.philips.platform.uappdemo.UappDemoSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoUFWState extends BaseState {

    private Context context;

    public DemoUFWState() {
        super(AppStates.TESTUAPP);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        UappDemoInterface uAppInterface = new UappDemoInterface();
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication)context.getApplicationContext();
        UappDemoDependencies uappDependencies = new UappDemoDependencies(appFrameworkApplication.getAppInfra());
        uAppInterface.init(uappDependencies, new UappDemoSettings(context.getApplicationContext()));// pass App-infra instance instead of null
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
