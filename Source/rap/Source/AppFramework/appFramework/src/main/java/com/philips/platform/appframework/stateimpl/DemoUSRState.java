package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import androidx.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.urdemo.URDemouAppDependencies;
import com.philips.platform.urdemo.URDemouAppInterface;
import com.philips.platform.urdemo.URDemouAppSettings;

/**
 * Created by philips on 30/03/17.
 */

public class DemoUSRState extends DemoBaseState {

    private Context context;

    public DemoUSRState() {
        super(AppStates.TESTUR);
    }


    @Override
    public void navigate(UiLauncher uiLauncher) {
        URDemouAppInterface uAppInterface = getUrDemouAppInterface();

        uAppInterface.init(new URDemouAppDependencies(((AppFrameworkApplication)context.getApplicationContext()).getAppInfra()), new URDemouAppSettings(context.getApplicationContext()));
        uAppInterface.launch(new ActivityLauncher(context, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDLSThemeConfiguration(context.getApplicationContext()), 0, null), null);

    }

    @NonNull
    protected URDemouAppInterface getUrDemouAppInterface() {
        return new URDemouAppInterface();
    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
