/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivityAbstract;
import com.philips.platform.baseapp.screens.introscreen.LaunchView;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * AbstractOnboardingBaseFragment is the <b>Base class</b> for all fragments which comes
 * under onboarding journey.
 */
public abstract class AbstractOnboardingBaseFragment extends Fragment implements LaunchView {
    public final String TAG = AbstractOnboardingBaseFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RALog.d(TAG," onCreate called");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showActionBar() {
        final LaunchActivityAbstract launchActivity = (LaunchActivityAbstract) getActivity();
        launchActivity.showActionBar();
    }

    @Override
    public void hideActionBar() {
        final LaunchActivityAbstract launchActivity = (LaunchActivityAbstract) getActivity();
        launchActivity.hideActionBar();
    }

    @Override
    public void finishActivityAffinity() {
        RALog.d(TAG," finishActivityAffinity called");
        final LaunchActivityAbstract launchActivity = (LaunchActivityAbstract) getActivity();
        launchActivity.finishAffinity();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (LaunchActivityAbstract) getActivity();
    }

    @Override
    public int getContainerId() {
        return R.id.welcome_frame_container;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

}
