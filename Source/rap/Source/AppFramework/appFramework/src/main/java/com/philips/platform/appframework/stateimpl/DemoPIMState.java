package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.iap.demouapp.IapDemoAppSettings;
import com.iap.demouapp.IapDemoUAppDependencies;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.pim.demouapp.PimDemoUAppInterface;

/**
 * Created by philips on 30/03/17.
 */

public class DemoPIMState extends DemoBaseState {

    private Context appContext;

    public DemoPIMState() {
        super(AppStates.TESTPIM);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        PimDemoUAppInterface pimDemoUAppDependencies = getPimDemoUAppInterface();
        pimDemoUAppDependencies.init(new IapDemoUAppDependencies(((AppFrameworkApplication)appContext.getApplicationContext()).getAppInfra()), new IapDemoAppSettings(appContext));
        pimDemoUAppDependencies.launch(new ActivityLauncher(appContext, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(appContext.getApplicationContext()), 0, null), null);
    }

    @NonNull
    protected PimDemoUAppInterface getPimDemoUAppInterface() {
        return new PimDemoUAppInterface();
    }

    @Override
    public void init(Context context) {
        appContext = context;
    }

    @Override
    public void updateDataModel() {

    }
}
