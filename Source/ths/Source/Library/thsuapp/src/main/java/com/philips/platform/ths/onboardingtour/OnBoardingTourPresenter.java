/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.ths.onboardingtour;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_1;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_2;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_3;
import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_4;


class OnBoardingTourPresenter implements THSBasePresenter {
    public static final String TAG = OnBoardingTourPresenter.class.getSimpleName();

    private OnBoardingTourFragment onBoardingTourFragment;

    OnBoardingTourPresenter(OnBoardingTourFragment onBoardingTourFragment) {
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

    List<OnBoardingTourContentModel> createOnBoardingContent() {

        List<OnBoardingTourContentModel> onBoardingTourContentModelList=new ArrayList<>();
        OnBoardingTourContentModel onBoardingTourContentModel1 = createOnBoardingTourContentModel1();
        OnBoardingTourContentModel onBoardingTourContentModel2 = createOnBoardingTourContentModel2();
        OnBoardingTourContentModel onBoardingTourContentModel3 = createOnBoardingTourContentModel3();
        OnBoardingTourContentModel onBoardingTourContentModel4 = createOnBoardingTourContentModel4();

        onBoardingTourContentModelList.add(onBoardingTourContentModel1);
        onBoardingTourContentModelList.add(onBoardingTourContentModel2);
        onBoardingTourContentModelList.add(onBoardingTourContentModel3);
        onBoardingTourContentModelList.add(onBoardingTourContentModel4);

        return onBoardingTourContentModelList;
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel1() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 64, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(65, onBoardingTourFragment.getString(R.string.ths_onboarding_one_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        return new OnBoardingTourContentModel(R.string.ths_onboarding_one_text, R.mipmap.onboarding_tour_one, spanValues, ON_BOARDING_PAGE_1);

    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel2() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 23, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(25, 52, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(53, 65, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(66, 73, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(75, onBoardingTourFragment.getString(R.string.ths_onboarding_two_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        return new OnBoardingTourContentModel(R.string.ths_onboarding_two_text, R.mipmap.onboarding_tour_two, spanValues, ON_BOARDING_PAGE_2);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel3() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 31, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(32, 53, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(55, 100, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(101, onBoardingTourFragment.getString(R.string.ths_onboarding_three_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        return new OnBoardingTourContentModel(R.string.ths_onboarding_three_text, R.mipmap.onboarding_tour_three, spanValues, ON_BOARDING_PAGE_3);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel4() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 28, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(29, 35, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(36, 43, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(44, onBoardingTourFragment.getString(R.string.ths_onboarding_four_text).length()-1, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        return new OnBoardingTourContentModel(R.string.ths_onboarding_four_text, R.mipmap.onboarding_tour_four, spanValues, ON_BOARDING_PAGE_4);
    }

    private void launchPreWelcomeScreen() {
        onBoardingTourFragment.popSelfBeforeTransition();
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        onBoardingTourFragment.addFragment(thsPreWelcomeFragment, THSPreWelcomeFragment.TAG, null, false);
    }
}
