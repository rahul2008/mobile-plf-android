/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappdemo.screens.introscreen.LaunchActivity;
import com.philips.platform.uappdemo.screens.introscreen.UappLaunchView;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * UappOnBoardingBaseFragment is the <b>Base class</b> for all fragments which comes
 * under onboarding journey.
 */
public abstract class UappOnBoardingBaseFragment extends Fragment implements UappLaunchView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showActionBar() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.showActionBar();
    }

    @Override
    public void hideActionBar() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.hideActionBar();
    }

    public void finishActivity() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.finish();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (LaunchActivity) getActivity();
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
