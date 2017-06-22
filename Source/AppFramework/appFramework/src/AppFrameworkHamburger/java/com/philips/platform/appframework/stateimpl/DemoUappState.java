package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoUappState extends BaseState {

    private Context context;

    public DemoUappState() {
        super(AppStates.TESTUAPP);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
//        UappDemouAppInterface uAppInterface = new UappDemouAppInterface();
//        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication)context.getApplicationContext();
//        UappDemouAppDependencies uappDependencies = new UappDemouAppDependencies(appFrameworkApplication.getAppInfra());
//        uAppInterface.init(uappDependencies, new UappDemouAppSettings(context.getApplicationContext()));// pass App-infra instance instead of null
//        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);

    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
