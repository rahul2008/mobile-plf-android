package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import androidx.annotation.NonNull;

import com.ecs.demouapp.integration.EcsDemoAppSettings;
import com.ecs.demouapp.integration.EcsDemoUAppDependencies;
import com.ecs.demouapp.integration.EcsDemoUAppInterface;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 30/03/17.
 */

public class DemoECSState extends DemoBaseState {

    private Context appContext;
    private final String TAG = DemoECSState.class.getSimpleName();

    public DemoECSState() {
        super(AppStates.TESTECS);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        EcsDemoUAppInterface uAppInterface = getEcsDemoUAppInterface();
        try {
            uAppInterface.init(new EcsDemoUAppDependencies(((AppFrameworkApplication) appContext.getApplicationContext()).getAppInfra()), new EcsDemoAppSettings(appContext));
        }catch (RuntimeException ex){
            RALog.d(TAG,ex.getMessage());
        }
        uAppInterface.launch(new ActivityLauncher(appContext, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(appContext.getApplicationContext()), 0, null), null);
    }

    @NonNull
    protected EcsDemoUAppInterface getEcsDemoUAppInterface() {
        return new EcsDemoUAppInterface();
    }

    @Override
    public void init(Context context) {
        appContext = context;
    }

    @Override
    public void updateDataModel() {

    }
}
