package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.urdemo.URDemouAppDependencies;
import com.philips.platform.urdemo.URDemouAppInterface;
import com.philips.platform.urdemo.URDemouAppSettings;

/**
 * Created by philips on 30/03/17.
 */

public class DemoURState extends BaseState {

    private Context context;

    public DemoURState() {
        super(AppStates.TESTUR);
    }


    @Override
    public void navigate(UiLauncher uiLauncher) {
        URDemouAppInterface uAppInterface = new URDemouAppInterface();
        AppInfraInterface appInfraInterface = new AppInfra.Builder().build(context.getApplicationContext());
        uAppInterface.init(new URDemouAppDependencies(appInfraInterface), new URDemouAppSettings(context.getApplicationContext()));
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
