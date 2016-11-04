/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.introscreen;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.stateimpl.URStateListener;
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
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        welcomeView.showActionBar();
        String eventState = getEventState(componentID);
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.WELCOME, eventState);
        fragmentLauncher = getFragmentLauncher();
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        if (!baseState.getStateID().equals(BaseAppState.REGISTRATION)) {
            welcomeView.finishActivityAffinity();
            baseState.setPresenter(this);
            baseState.navigate(fragmentLauncher);
        }
    }

    private String getEventState(final int componentID) {
        switch (componentID) {
            case Constants.BACK_BUTTON_CLICK_CONSTANT:
                return WELCOME_HOME;
            case USER_REGISTRATION_STATE:
                return WELCOME_REGISTRATION;
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
        welcomeView.hideActionBar();
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(null,null);
        fragmentLauncher = getFragmentLauncher();
        UIStateData homeStateData = new UIStateData();
        homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
        baseState.setUiStateData(homeStateData);
        baseState.navigate(fragmentLauncher);
    }

    @Override
    public void onStateComplete(BaseState baseState) {
        appFrameworkApplication = (AppFrameworkApplication) welcomeView.getFragmentActivity().getApplicationContext();
        String eventId = getEventState(Constants.BACK_BUTTON_CLICK_CONSTANT);
        this.baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.WELCOME, eventId);
        fragmentLauncher = getFragmentLauncher();
        this.baseState.setPresenter(this);
        welcomeView.finishActivityAffinity();
        this.baseState.navigate(fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }
}
