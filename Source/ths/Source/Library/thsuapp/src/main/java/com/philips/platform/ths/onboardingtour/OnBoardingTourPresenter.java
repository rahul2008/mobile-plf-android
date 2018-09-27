/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.onboarding.OnBoardingFragment;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_1;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_2;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_3;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_4;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_5;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;


public class OnBoardingTourPresenter implements THSBasePresenter {
    public static final String TAG = OnBoardingTourPresenter.class.getSimpleName();

    private OnBoardingTourFragment onBoardingTourFragment;
    public static final String ARG_PAGER_FLOW = "isFromPager";

    OnBoardingTourPresenter(OnBoardingTourFragment onBoardingTourFragment) {
        this.onBoardingTourFragment = onBoardingTourFragment;
    }

    @Override
    public void onEvent(final int componentID) {


        if (componentID == R.id.welcome_rightarrow) {

        }
        if (componentID == R.id.welcome_leftarrow) {

        }
        if (componentID == R.id.welcome_skip_button) {
            launchTermsAndConditionsScreen();
        }
        if (componentID == R.id.btn_startnow) {
            launchTermsAndConditionsScreen();
        }
        if(componentID == R.id.btn_termsConditions){
            if (THSManager.getInstance().isReturningUser()) {
                THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
                onBoardingTourFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null, false);
            } else {
                THSRegistrationFragment thsRegistrationFragment = new THSRegistrationFragment();
                onBoardingTourFragment.addFragment(thsRegistrationFragment, THSRegistrationFragment.TAG, null, false);
            }
        }
    }

    List<OnBoardingTourContentModel> createOnBoardingContent() {

        List<OnBoardingTourContentModel> onBoardingTourContentModelList = new ArrayList<>();
        OnBoardingTourContentModel onBoardingTourContentModel1 = createOnBoardingTourContentModel1();
        OnBoardingTourContentModel onBoardingTourContentModel2 = createOnBoardingTourContentModel2();
        OnBoardingTourContentModel onBoardingTourContentModel3 = createOnBoardingTourContentModel3();
        OnBoardingTourContentModel onBoardingTourContentModel4 = createOnBoardingTourContentModel4();
        OnBoardingTourContentModel onBoardingTourContentModel5 = createOnBoardingTourContentModel5();

        onBoardingTourContentModelList.add(onBoardingTourContentModel1);
        onBoardingTourContentModelList.add(onBoardingTourContentModel2);
        onBoardingTourContentModelList.add(onBoardingTourContentModel3);
        onBoardingTourContentModelList.add(onBoardingTourContentModel4);
        onBoardingTourContentModelList.add(onBoardingTourContentModel5);

        return onBoardingTourContentModelList;
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel1() {
        final OnBoardingTourContentModel onBoardingTourContentModel = new OnBoardingTourContentModel(R.string.ths_onboarding_one_text, R.mipmap.onboarding_tour_one, ON_BOARDING_PAGE_1, R.string.ths_onboarding_title_one);
        onBoardingTourContentModel.setIsAmwellLogoVisible(true);
        return onBoardingTourContentModel;
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel2() {
        return new OnBoardingTourContentModel(R.string.ths_onboarding_two_text, R.mipmap.onboarding_tour_two, ON_BOARDING_PAGE_2, R.string.ths_onboarding_title_two);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel3() {
        return new OnBoardingTourContentModel(R.string.ths_onboarding_three_text, R.mipmap.onboarding_tour_three, ON_BOARDING_PAGE_3, R.string.ths_onboarding_title_three);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel4() {
        return new OnBoardingTourContentModel(R.string.ths_onboarding_four_text, R.mipmap.onboarding_tour_four, ON_BOARDING_PAGE_4, R.string.ths_onboarding_title_four);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel5() {
        return new OnBoardingTourContentModel(R.string.ths_onboarding_four_text, 0, ON_BOARDING_PAGE_5, 0);
    }

    private void launchTermsAndConditionsScreen() {
        String pageTitle = onBoardingTourFragment.getOnBoardingTourPagerAdapter().getPageTitle(onBoardingTourFragment.getPagePosition());
        THSTagUtils.doTrackActionWithInfo(pageTitle,THS_SPECIAL_EVENT,"skipOnboarding");
        onBoardingTourFragment.popSelfBeforeTransition();
        OnBoardingFragment onBoardingFragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PAGER_FLOW, false);
        onBoardingTourFragment.addFragment(onBoardingFragment, OnBoardingFragment.TAG, args, false);
    }
}
