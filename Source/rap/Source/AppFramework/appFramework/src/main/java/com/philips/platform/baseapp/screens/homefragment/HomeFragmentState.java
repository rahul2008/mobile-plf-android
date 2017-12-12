/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.homefragment;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeFragmentState extends BaseState {
    public static final String TAG =  HomeFragmentState.class.getSimpleName();

    public HomeFragmentState() {
        super(AppStates.HOME_FRAGMENT);
    }

    /**
     * for Navigation
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG," navigate Called ");

        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).handleFragmentBackStack( new HomeFragment(), HomeFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
