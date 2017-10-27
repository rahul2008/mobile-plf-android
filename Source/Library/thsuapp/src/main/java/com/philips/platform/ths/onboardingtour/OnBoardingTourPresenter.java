/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.ths.onboardingtour;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;

import java.util.ArrayList;
import java.util.List;


public class OnBoardingTourPresenter implements THSBasePresenter {
    public static String TAG = OnBoardingTourPresenter.class.getSimpleName();

    private OnBoardingTourFragment onBoardingTourFragment;

    public OnBoardingTourPresenter(OnBoardingTourFragment onBoardingTourFragment) {
        this.onBoardingTourFragment = onBoardingTourFragment;
    }

    @Override
    public void onEvent(final int componentID) {


        if(componentID == R.id.welcome_rightarrow){

        }
        if(componentID == R.id.welcome_leftarrow){

        }
        if(componentID == R.id.welcome_start_registration_button){
            launchPreWelcomeScreen();
        }
        if(componentID == R.id.welcome_skip_button){
            launchPreWelcomeScreen();
        }
    }

    public  List<OnBoardingTourContentModel> createOnBoardingContent() {

        List<OnBoardingTourContentModel> onBoardingTourContentModelList=new ArrayList<>();
        OnBoardingTourContentModel onBoardingTourContentModel1 = new OnBoardingTourContentModel(R.string.onboarding_one_text, R.mipmap.onboarding_tour_one);
        OnBoardingTourContentModel onBoardingTourContentModel2 = new OnBoardingTourContentModel(R.string.onboarding_two_text, R.mipmap.onboarding_tour_two);
        OnBoardingTourContentModel onBoardingTourContentModel3 = new OnBoardingTourContentModel(R.string.onboarding_three_text, R.mipmap.onboarding_tour_three);
        OnBoardingTourContentModel onBoardingTourContentModel4 = new OnBoardingTourContentModel(R.string.onboarding_four_text, R.mipmap.onboarding_tour_four);

        onBoardingTourContentModelList.add(onBoardingTourContentModel1);
        onBoardingTourContentModelList.add(onBoardingTourContentModel2);
        onBoardingTourContentModelList.add(onBoardingTourContentModel3);
        onBoardingTourContentModelList.add(onBoardingTourContentModel4);

        return onBoardingTourContentModelList;
    }

    private void launchPreWelcomeScreen() {
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        onBoardingTourFragment.addFragment(thsPreWelcomeFragment, THSRegistrationFragment.TAG, null, false);
    }
}
