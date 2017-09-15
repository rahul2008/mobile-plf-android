/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HamburgerActivityState extends BaseState {
    public static final String TAG = HamburgerActivityState.class.getSimpleName();

    public HamburgerActivityState() {
        super(AppStates.HAMBURGER_HOME);
    }

    /**
     * Navigate to HamburgerActivity
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d( TAG, " navigate  ");
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        fragmentLauncher.getFragmentActivity().startActivity(new Intent(fragmentLauncher.getFragmentActivity(), HamburgerActivity.class));
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
