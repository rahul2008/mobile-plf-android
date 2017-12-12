/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.debugtest;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.settingscreen.SettingsFragmentState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * This class if used for dynamic configuration of Environment of User registration
 */
public class DebugTestFragmentState extends BaseState {
    public static final String TAG = SettingsFragmentState.class.getSimpleName();

    public DebugTestFragmentState() {
        super(AppStates.DEBUG);
    }

    /**
     * Navigate to the fragment
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG," navigate");
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new DebugTestFragment(), DebugTestFragment.TAG, Constants.ADD_FRAGMENT_WITH_BACKSTACK);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
