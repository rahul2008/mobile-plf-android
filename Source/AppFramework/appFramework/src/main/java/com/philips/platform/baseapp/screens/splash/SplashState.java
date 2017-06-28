/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class SplashState extends BaseState {
    public static final String TAG = SplashState.class.getSimpleName();

    public SplashState() {
        super(AppStates.SPLASH);
    }

    @Override
    public void navigate(final UiLauncher uiLauncher) {
        RALog.d(TAG," navigate called ");
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                addFragment(new SplashFragment(), SplashFragment.TAG);
    }

    @Override
    public void init(final Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
