/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.util.Log;

import com.philips.cdp.devicepair.uappdependencies.WifiCommLibUappSettings;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.DefaultCommlibUappDependencies;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 29/06/17.
 */

public class DemoCMLState extends BaseState {

    private static final String TAG = DemoCMLState.class.getSimpleName();
    private Context context;

    public DemoCMLState() {
        super(AppStates.TESTDICOMM);
    }

    private CommlibUapp uAppInterface;

    @Override
    public void navigate(UiLauncher uiLauncher) {
        //TODO:Needs to launch comm lib demo micro app
        uAppInterface = getCommLibUApp();
        if (uAppInterface != null) {
            Log.d(TAG, "CommlibUApp is null");
            try {
                uAppInterface.init(new DefaultCommlibUappDependencies(context.getApplicationContext()), new WifiCommLibUappSettings(context.getApplicationContext()));
            }// pass App-infra instance instead of null
            catch (UnsatisfiedLinkError error) {
                RALog.d(TAG, "Not able to find native implementation");
            }
            uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
        }


    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    public CommlibUapp getCommLibUApp() {
        return CommlibUapp.get();
    }

    @Override
    public void updateDataModel() {

    }
}
