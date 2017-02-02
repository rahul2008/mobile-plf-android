/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;

import android.app.Activity;

import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;


public class UserRegistrationOnBoardingState extends UserRegistrationState implements UserRegistrationUIEventListener {

    private BaseState baseState;
    /**
     * AppFlowState constructor
     *
     */
    public UserRegistrationOnBoardingState() {
        super(AppStates.ON_BOARDING_REGISTRATION);
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            try {
                baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), "URComplete");
            } catch (NoEventFoundException e) {
                e.printStackTrace();
            }
            if (null != baseState) {
                getFragmentActivity().finish();
                baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.welcome_frame_container, (ActionBarListener) getFragmentActivity()));
                baseState = null;
            }
        }
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void onUserLogoutSuccess() {
    }

    @Override
    public void onUserLogoutFailure() {
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
    }


}
