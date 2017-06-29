package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 29/06/17.
 */

public class DemoDicommClientState extends BaseState {

    private Context context;
    public DemoDicommClientState() {
        super(AppStates.TESTDICOMM);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
//        FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
//        Context activityContext = fragmentLauncher.getFragmentActivity();
//        CommlibUapp uAppInterface = new CommlibUapp();
//        uAppInterface.init(new DefaultCommlibUappDependencies(activityContext), new UappSettings(activityContext));// pass App-infra instance instead of null
//        uAppInterface.launch(uiLauncher, new UappLaunchInput());
    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
