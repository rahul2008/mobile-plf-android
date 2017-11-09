package com.philips.platform.ths.onboarding;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.onboardingtour.OnBoardingTourFragment;
import com.philips.platform.ths.onboardingtour.OnBoardingTourPageFragment;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;

/**
 * Created by philips on 10/25/17.
 */

public class OnBoardingPresenter implements THSBasePresenter {

    private final OnBoardingFragment onBoardingFragment;


    public OnBoardingPresenter(OnBoardingFragment onBoardingFragment) {
        this.onBoardingFragment = onBoardingFragment;
    }

    @Override
    public void onEvent(int componentID) {

        if(componentID == R.id.tv_skip){
            launchPreWelcomeScreen();
        }
        if(componentID==R.id.btn_take_tour){
            onBoardingFragment.popSelfBeforeTransition();
            onBoardingFragment.addFragment(new OnBoardingTourFragment(), OnBoardingTourFragment.TAG, null, true);
        }
    }

    private void launchPreWelcomeScreen() {
        onBoardingFragment.popSelfBeforeTransition();
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        onBoardingFragment.addFragment(thsPreWelcomeFragment, THSRegistrationFragment.TAG, null, false);
    }
}
