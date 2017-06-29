package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.iap.demouapp.IapDemoAppSettings;
import com.iap.demouapp.IapDemoUAppDependencies;
import com.iap.demouapp.IapDemoUAppInterface;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoIAPState extends BaseState {

    private Context appContext;

    public DemoIAPState() {
        super(AppStates.TESTIAP);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        IapDemoUAppInterface uAppInterface = new IapDemoUAppInterface();
        uAppInterface.init(new IapDemoUAppDependencies(((AppFrameworkApplication)appContext.getApplicationContext()).getAppInfra()), new IapDemoAppSettings(appContext));
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }

    @Override
    public void init(Context context) {
        appContext = context;
    }

    @Override
    public void updateDataModel() {

    }
}
