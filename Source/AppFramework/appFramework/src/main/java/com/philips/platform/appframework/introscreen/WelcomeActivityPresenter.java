/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.introscreen;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.jsonstates.EventStates;
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
    private UIState uiState;
    private FragmentLauncher fragmentLauncher;

    public WelcomeActivityPresenter(WelcomeView welcomeView) {
        super(welcomeView);
        this.welcomeView = welcomeView;
    }

    /**
     * Handles the onclick of Welcome Skip and Done button
     *
     * @param componentID : takes compenent Id
     */
    @Override
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        welcomeView.showActionBar();
        EventStates eventId = getEventID(componentID);
        uiState = FlowManager.getInstance(appFrameworkApplication).getNextState(AppStates.WELCOME, eventId);
        fragmentLauncher = getFragmentLauncher();
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        if (appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == (UIState.UI_USER_REGISTRATION_STATE)) {
            welcomeView.finishActivityAffinity();
            uiState.setPresenter(this);
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            uiState.navigate(fragmentLauncher);
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

    private EventStates getEventID(final int componentID) {
        switch (componentID) {
            case Constants.BACK_BUTTON_CLICK_CONSTANT:
                return EventStates.WELCOME_HOME;
            case USER_REGISTRATION_STATE:
                return EventStates.REGISTRATION;
        }
        return null;
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
        final SharedPreferenceUtility sharedPreferenceUtility = getSharedPreferenceUtility();
        if (sharedPreferenceUtility.getPreferenceBoolean(Constants.DONE_PRESSED) || appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_USER_REGISTRATION_STATE) {
            welcomeView.showActionBar();
            setState(UIState.UI_USER_REGISTRATION_STATE);
            EventStates eventId = getEventID(USER_REGISTRATION_STATE);
            uiState = FlowManager.getInstance(appFrameworkApplication).getNextState(AppStates.WELCOME, eventId);
            fragmentLauncher = getFragmentLauncher();
            uiState.setPresenter(this);
            ((UserRegistrationState) uiState).registerUIStateListener(this);
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            uiState.navigate(fragmentLauncher);
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
        EventStates eventId = getEventID(Constants.BACK_BUTTON_CLICK_CONSTANT);
        this.uiState = FlowManager.getInstance(appFrameworkApplication).getNextState(AppStates.WELCOME, eventId);
        fragmentLauncher = getFragmentLauncher();
        this.uiState.setPresenter(this);
        welcomeView.finishActivityAffinity();
        this.uiState.navigate(fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
