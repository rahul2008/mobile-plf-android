/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package philips.app.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappframework.listener.ActionBarListener;

import philips.app.R;
import philips.app.introscreen.LaunchActivity;
import philips.app.introscreen.LaunchView;

/**
 * OnboardingBaseFragment is the <b>Base class</b> for all fragments which comes
 * under onboarding journey.
 */
public abstract class OnboardingBaseFragment extends Fragment implements LaunchView {

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

    @Override
    public void finishActivityAffinity() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.finishAffinity();
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
