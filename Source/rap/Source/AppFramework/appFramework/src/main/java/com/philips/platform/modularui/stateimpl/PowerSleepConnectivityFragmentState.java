/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.connectivitypowersleep.PowerSleepConnectivityFragment;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class PowerSleepConnectivityFragmentState extends BaseState {
    public final String TAG = PowerSleepConnectivityFragmentState.class.getSimpleName();

    public PowerSleepConnectivityFragmentState() {
        super(AppStates.POWER_SLEEP_CONNECTIVITY);
    }

    /**
     * to navigate
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG, " navigate called ");
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack(new PowerSleepConnectivityFragment(), PowerSleepConnectivityFragment.TAG, getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {
    }

    @Override
    public void updateDataModel() {

    }

}
