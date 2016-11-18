/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;

import android.app.Activity;

import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.screens.datasevices.registration.UserRegistrationFacadeImpl;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class UserRegistrationSettingsState extends UserRegistrationState implements UserRegistrationUIEventListener,UserRegistrationListener {

    private BaseState baseState;
    private String SETTINGS_LOGOUT = "logout";
    /**
     * AppFlowState constructor
     *
     */
    public UserRegistrationSettingsState() {
        super(AppStates.SETTINGS_REGISTRATION);
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.SPLASH, "splash_home");
            if (null != baseState) {
                getFragmentActivity().finish();
                baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.frame_container, (ActionBarListener) getFragmentActivity()));
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
        baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.SETTINGS, SETTINGS_LOGOUT);
        baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.frame_container, (ActionBarListener) getFragmentActivity()));
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
