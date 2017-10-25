package com.philips.platform.ths.onboarding;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.onboardingtour.OnBoardingTourFragment;
import com.philips.platform.ths.onboardingtour.OnBoardingTourPageFragment;
import com.philips.platform.ths.practice.THSPracticeFragment;

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

        }
        if(componentID==R.id.btn_take_tour){
            onBoardingFragment.addFragment(new OnBoardingTourFragment(), OnBoardingTourFragment.TAG, null, false);
        }
    }
}
