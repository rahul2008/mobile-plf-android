package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.neu.demouapp.NeuraDemoUAppInterface;
import com.philips.platform.neu.demouapp.NeuraDemouAppDependencies;
import com.philips.platform.neu.demouapp.NeuraDemouAppSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class DemoNeuraState extends DemoBaseState {
    private Context context;

    public DemoNeuraState() {
        super(AppStates.TESTNEURADEMO);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        NeuraDemoUAppInterface uAppInterface = getNeuraDemoUAppInterface();
        AppFrameworkApplication applicationContext = (AppFrameworkApplication) context.getApplicationContext();
        uAppInterface.init(new NeuraDemouAppDependencies(applicationContext.getAppInfra()),new NeuraDemouAppSettings(applicationContext));
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(applicationContext), 0, null), null);
    }

    private NeuraDemoUAppInterface getNeuraDemoUAppInterface() {
        return new NeuraDemoUAppInterface();
    }

    @Override
    public void init(Context context) {
        this.context = context;

    }

    @Override
    public void updateDataModel() {

    }
}
