/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.introscreen;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.URStateListener;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Welcome presenter handles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class WelcomeActivityPresenter extends UIBasePresenter implements URStateListener {

    private static final int USER_REGISTRATION_STATE = 889;
    private WelcomeView welcomeView;
    private AppFrameworkApplication appFrameworkApplication;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private UIState uiState;
    private FragmentLauncher fragmentLauncher;

    public WelcomeActivityPresenter(WelcomeView welcomeView) {
        super(welcomeView);
        this.welcomeView = welcomeView;
    }

    /**
     * Handles the onclick of Welcome Skip and Done button
     * @param componentID : takes compenent Id
     *
     */
    @Override
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        welcomeView.showActionBar();
        uiState = getUiState(componentID);
        fragmentLauncher = getFragmentLauncher();
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        if (appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == (UIState.UI_USER_REGISTRATION_STATE)) {
            welcomeView.finishActivityAffinity();
            uiState.setPresenter(this);
            appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
        }
    }

    protected UIState getUiState(final int componentID) {
        switch (componentID) {
            case Constants.BACK_BUTTON_CLICK_CONSTANT:
                uiState = new HomeActivityState();
                break;
            case USER_REGISTRATION_STATE:
                uiState = new UserRegistrationState();
                break;
        }
        return uiState;
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(welcomeView.getFragmentActivity(), welcomeView.getContainerId(), welcomeView.getActionBarListener());
        return fragmentLauncher;
    }

    /**
     * Takes care of handling whether to show user regitration after the splash screen has loaded or to show Welcome fragments if onboarding was skipped at the time of first launch
     */
    @Override
    public void onLoad() {
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        sharedPreferenceUtility = getSharedPreferenceUtility();
        if (sharedPreferenceUtility.getPreferenceBoolean(Constants.DONE_PRESSED)|| appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_USER_REGISTRATION_STATE) {
            welcomeView.showActionBar();
            setState(UIState.UI_USER_REGISTRATION_STATE);
            uiState = getUiState(USER_REGISTRATION_STATE);
            fragmentLauncher = getFragmentLauncher();
            uiState.setPresenter(this);
            ((UserRegistrationState)uiState).registerUIStateListener(this);
            appFrameworkApplication.getFlowManager().navigateToState(uiState, this.fragmentLauncher);
        } else {
            setState(UIState.UI_WELCOME_STATE);
            appFrameworkApplication.getFlowManager().getCurrentState().setStateID(UIState.UI_WELCOME_STATE);
            welcomeView.hideActionBar();
            welcomeView.loadWelcomeFragment();
        }
    }

    @NonNull
    protected SharedPreferenceUtility getSharedPreferenceUtility() {
        return new SharedPreferenceUtility(welcomeView.getFragmentActivity());
    }

    @Override
    public void onStateComplete(UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        this.uiState = getUiState(Constants.BACK_BUTTON_CLICK_CONSTANT);
        fragmentLauncher = getFragmentLauncher();
        this.uiState.setPresenter(this);
        welcomeView.finishActivityAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
