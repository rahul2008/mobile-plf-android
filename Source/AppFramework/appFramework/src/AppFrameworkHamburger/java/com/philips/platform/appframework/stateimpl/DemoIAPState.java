package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
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
//        IAPDemouAppInterface uAppInterface = new IAPDemouAppInterface();
//        uAppInterface.initialise(null, new IAPDemouAppSettings(appContext));
//        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }

    @Override
    public void init(Context context) {
        appContext = context;
    }

    @Override
    public void updateDataModel() {

    }
}
