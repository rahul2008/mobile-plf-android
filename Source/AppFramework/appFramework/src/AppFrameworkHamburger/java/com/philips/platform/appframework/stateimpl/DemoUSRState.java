package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.urdemo.URDemouAppDependencies;
import com.philips.platform.urdemo.URDemouAppInterface;
import com.philips.platform.urdemo.URDemouAppSettings;

/**
 * Created by philips on 30/03/17.
 */

public class DemoUSRState extends BaseState {

    private Context context;

    public DemoUSRState() {
        super(AppStates.TESTUR);
    }


    @Override
    public void navigate(UiLauncher uiLauncher) {
        URDemouAppInterface uAppInterface = new URDemouAppInterface();

        uAppInterface.init(new URDemouAppDependencies(((AppFrameworkApplication)context.getApplicationContext()).getAppInfra()), new URDemouAppSettings(context.getApplicationContext()));
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
