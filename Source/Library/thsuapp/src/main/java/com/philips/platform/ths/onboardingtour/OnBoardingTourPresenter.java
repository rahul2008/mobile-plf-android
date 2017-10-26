/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.ths.onboardingtour;

import com.philips.platform.ths.R;
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

        /*rightArrow = (ImageView) view.findViewById(R.id.welcome_rightarrow);
        leftArrow = (ImageView) view.findViewById(R.id.welcome_leftarrow);
        doneButton = (Label) view.findViewById(R.id.welcome_start_registration_button);
        skipButton = (Label) view.findViewById(R.id.welcome_skip_button);*/

        if(componentID == R.id.welcome_rightarrow){

        }
        if(componentID == R.id.welcome_leftarrow){

        }
        if(componentID == R.id.welcome_start_registration_button){

        }
        if(componentID == R.id.welcome_skip_button){

        }
    }


    protected String getEventState(final int componentID) {

        return WELCOME_HOME;
    }

}
