/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen;

import android.support.annotation.NonNull;

import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.base.UIStateData;
import com.philips.platform.baseapp.screens.userregistration.URStateListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Welcome presenter handles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class LaunchActivityPresenter extends UIBasePresenter implements URStateListener {

    private static final int USER_REGISTRATION_STATE = 889;
    final String onAppStartEvent = "onAppStartEvent";
    private WelcomeView welcomeView;
    private AppFrameworkApplication appFrameworkApplication;
    private BaseState baseState;
    private FragmentLauncher fragmentLauncher;
    private String WELCOME_HOME = "welcome_home";
    private String WELCOME_REGISTRATION = "welcome_registration";

    public LaunchActivityPresenter(WelcomeView welcomeView) {
        super(welcomeView);
        this.welcomeView = welcomeView;
    }

    /**
     * Handles the onclick of Welcome Skip and Done button
     *
     * @param componentID : takes compenent Id
     */
    @Override
    public void onEvent(int componentID) {
        showActionBar();
        String eventState = getEventState(componentID);

        fragmentLauncher = getFragmentLauncher();
        if(componentID == 0) {
            hideActionBar();
            baseState = getApplicationContext().getTargetFlowManager().getNextState(null, null);
            baseState.setUiStateData(getUiStateData());
        } else {
            baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.WELCOME, eventState);
        }
        if (!baseState.getStateID().equals(AppStates.REGISTRATION)) {
            baseState.setPresenter(this);
            baseState.navigate(fragmentLauncher);
        }
    }

    protected void hideActionBar() {
        welcomeView.hideActionBar();
    }

    protected void finishActivity() {
        welcomeView.finishActivityAffinity();
    }

    protected void showActionBar() {
        welcomeView.showActionBar();
    }

    protected String getEventState(final int componentID) {
        switch (componentID) {
            case Constants.BACK_BUTTON_CLICK_CONSTANT:
                return WELCOME_HOME;
            case USER_REGISTRATION_STATE:
                return WELCOME_REGISTRATION;
            default:return null;
        }
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(welcomeView.getFragmentActivity(), welcomeView.getContainerId(), welcomeView.getActionBarListener());
        return fragmentLauncher;
    }

    @NonNull
    protected UIStateData getUiStateData() {
        UIStateData homeStateData = new UIStateData();
        homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
        return homeStateData;
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
    }
    @Override
    public void onStateComplete(BaseState baseState) {
        String eventId = getEventState(Constants.BACK_BUTTON_CLICK_CONSTANT);
        this.baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.WELCOME, eventId);
        fragmentLauncher = getFragmentLauncher();
        this.baseState.setPresenter(this);
        finishActivity();
        this.baseState.navigate(fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
