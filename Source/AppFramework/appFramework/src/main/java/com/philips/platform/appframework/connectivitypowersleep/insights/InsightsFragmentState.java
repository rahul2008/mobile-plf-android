/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 9/5/17.
 */

public class InsightsFragmentState extends BaseState {

    private static final String TAG = InsightsFragmentState.class.getSimpleName();

    private FragmentLauncher fragmentLauncher;;

    public InsightsFragmentState() {
        super(AppStates.INSIGHTS);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG, " navigate called ");
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AbstractAppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new InsightsFragment(), InsightsFragment.TAG, Constants.ADD_FRAGMENT_WITH_BACKSTACK);
    }

    @Override
    public void init(Context context) {
    }

    @Override
    public void updateDataModel() {
    }
}
