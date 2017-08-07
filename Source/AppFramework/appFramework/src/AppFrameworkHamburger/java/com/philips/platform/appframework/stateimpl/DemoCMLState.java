package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.util.Log;

import com.philips.cdp.devicepair.uappdependencies.WifiCommLibUappSettings;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.DefaultCommlibUappDependencies;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 29/06/17.
 */

public class DemoCMLState extends BaseState {

    private static final String TAG=DemoCMLState.class.getSimpleName();
    private Context context;
    public DemoCMLState() {
        super(AppStates.TESTDICOMM);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        //TODO:Needs to launch comm lib demo micro app
        CommlibUapp uAppInterface = CommlibUapp.get();
        if(uAppInterface==null) {
            Log.d(TAG, "CommlibUApp is null");
        }
        uAppInterface.init(new DefaultCommlibUappDependencies(context.getApplicationContext()),new WifiCommLibUappSettings(context.getApplicationContext()));// pass App-infra instance instead of null
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
