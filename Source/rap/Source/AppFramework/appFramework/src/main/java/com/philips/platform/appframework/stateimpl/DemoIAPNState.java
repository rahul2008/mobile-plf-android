package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ian.demouapp.IanDemoAppSettings;
import com.ian.demouapp.IanDemoUAppDependencies;
import com.ian.demouapp.IanDemoUAppInterface;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoIAPNState extends DemoBaseState {

    private Context appContext;
    private final String TAG = DemoIAPNState.class.getSimpleName();

    public DemoIAPNState() {
        super(AppStates.TESTIAPN);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        IanDemoUAppInterface uAppInterface = getIanDemoUAppInterface();
        try {
            uAppInterface.init(new IanDemoUAppDependencies(((AppFrameworkApplication) appContext.getApplicationContext()).getAppInfra()), new IanDemoAppSettings(appContext));
        }catch (RuntimeException ex){
            RALog.d(TAG,ex.getMessage());
        }
        uAppInterface.launch(new ActivityLauncher(appContext, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(appContext.getApplicationContext()), 0, null), null);
    }

    @NonNull
    protected IanDemoUAppInterface getIanDemoUAppInterface() {
        return new IanDemoUAppInterface();
    }

    @Override
    public void init(Context context) {
        appContext = context;
    }

    @Override
    public void updateDataModel() {

    }
}
