package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

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

public class DemoUFWState extends DemoBaseState {

    private Context context;

    public DemoUFWState() {
        super(AppStates.TESTUAPP);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        UappDemoInterface uAppInterface = getUappDemoInterface();
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication)context.getApplicationContext();
        UappDemoDependencies uappDependencies = new UappDemoDependencies(appFrameworkApplication.getAppInfra());
        uAppInterface.init(uappDependencies, new UappDemoSettings(context.getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null), null);

    }

    @NonNull
    protected UappDemoInterface getUappDemoInterface() {
        return new UappDemoInterface();
    }

    @Override
    public void init(Context context) {
        this.context=context;
    }



    @Override
    public void updateDataModel() {

    }
}
