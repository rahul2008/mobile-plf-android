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
        spanValues.add(new OnBoardingSpanValue(0, 26, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(27, 61, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(62, 84, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(88, 112, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(113, 128, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(129, onBoardingTourFragment.getString(R.string.onboarding_one_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        return new OnBoardingTourContentModel(R.string.onboarding_one_text, R.mipmap.onboarding_tour_one, spanValues, ON_BOARDING_PAGE_1);

    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel2() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 17, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(18, onBoardingTourFragment.getString(R.string.onboarding_two_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        return new OnBoardingTourContentModel(R.string.onboarding_two_text, R.mipmap.onboarding_tour_two, spanValues, ON_BOARDING_PAGE_2);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel3() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 26, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(27, 62, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(63, onBoardingTourFragment.getString(R.string.onboarding_three_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        return new OnBoardingTourContentModel(R.string.onboarding_three_text, R.mipmap.onboarding_tour_three, spanValues, ON_BOARDING_PAGE_3);
    }

    private OnBoardingTourContentModel createOnBoardingTourContentModel4() {
        List<OnBoardingSpanValue> spanValues = new ArrayList<>();
        spanValues.add(new OnBoardingSpanValue(0, 6, OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        spanValues.add(new OnBoardingSpanValue(7, 120, OnBoardingSpanValue.OnBoardingTypeface.BOOK));
        spanValues.add(new OnBoardingSpanValue(121, onBoardingTourFragment.getString(R.string.onboarding_four_text).length(), OnBoardingSpanValue.OnBoardingTypeface.BOLD));
        return new OnBoardingTourContentModel(R.string.onboarding_four_text, R.mipmap.onboarding_tour_four, spanValues, ON_BOARDING_PAGE_4);
    }

    private void launchPreWelcomeScreen() {
        onBoardingTourFragment.popSelfBeforeTransition();
        THSPreWelcomeFragment thsPreWelcomeFragment = new THSPreWelcomeFragment();
        onBoardingTourFragment.addFragment(thsPreWelcomeFragment, THSRegistrationFragment.TAG, null, false);
    }
}
