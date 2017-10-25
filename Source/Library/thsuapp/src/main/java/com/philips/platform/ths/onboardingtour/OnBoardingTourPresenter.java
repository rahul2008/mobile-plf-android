/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.ths.onboardingtour;

import com.philips.platform.ths.base.THSBasePresenter;


public class OnBoardingTourPresenter implements THSBasePresenter {
    public static String TAG = OnBoardingTourPresenter.class.getSimpleName();

    private String WELCOME_SKIP = "welcome_skip";
    private String WELCOME_DONE = "welcome_done";
    private String WELCOME_HOME = "welcome_home";

    private OnBoardingTourFragment onBoardingTourFragment;

    public OnBoardingTourPresenter(OnBoardingTourFragment onBoardingTourFragment) {
        this.onBoardingTourFragment = onBoardingTourFragment;
    }

    @Override
    public void onEvent(final int componentID) {

    }


    protected String getEventState(final int componentID) {

        return WELCOME_HOME;
    }

}
