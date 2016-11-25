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
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.BaseUiFlowManager;
import com.philips.platform.baseapp.screens.dataservices.registration.UserRegistrationFacadeImpl;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class UserRegistrationSplashState extends UserRegistrationState implements UserRegistrationUIEventListener {

    private BaseState baseState;
    /**
     * AppFlowState constructor
     *
     */
    public UserRegistrationSplashState() {
        super(AppStates.SPLASH_REGISTRATION);
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            BaseUiFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.SPLASH), "splash_home");
            if (null != baseState) {
                getFragmentActivity().finish();
                baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.welcome_frame_container, (ActionBarListener) getFragmentActivity()));
            }
            UserRegistrationFacadeImpl userRegistrationFacade = new UserRegistrationFacadeImpl(activity, getUserObject(activity));
            userRegistrationFacade.clearUserData();
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
        UserRegistrationFacadeImpl userRegistrationFacade = new UserRegistrationFacadeImpl(getFragmentActivity(), getUserObject(getFragmentActivity()));
        userRegistrationFacade.clearUserData();
    }

    @Override
    public void onUserLogoutFailure() {
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        UserRegistrationFacadeImpl userRegistrationFacade = new UserRegistrationFacadeImpl(getFragmentActivity(), getUserObject(getFragmentActivity()));
        userRegistrationFacade.clearUserData();
    }


}
