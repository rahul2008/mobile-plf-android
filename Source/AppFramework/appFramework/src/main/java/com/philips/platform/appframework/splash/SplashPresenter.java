/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.splash;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.introscreen.WelcomeView;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Spalsh presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UIBasePresenter {
    private final WelcomeView uiView;
    private String APP_START = "onAppStartEvent";

    public SplashPresenter(WelcomeView uiView) {
        super(uiView);
        this.uiView = uiView;
        setState(BaseState.UI_SPLASH_STATE);
    }

    @Override
    public void onClick(int componentID) {

    }

    /**
     * The methods takes decision to load which next state needs to be loaded after splash screen
     * Depending upon the User registration is compelted on not state will change
     *
     */
    @Override
    public void onLoad() {
        final AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) uiView.getFragmentActivity().getApplicationContext();
        final BaseState baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.SPLASH, APP_START);
        // TODO: Deepthi, container id seems to be hardcoded here.
        final FragmentLauncher fragmentLauncher = new FragmentLauncher(uiView.getFragmentActivity(), uiView.getContainerId(), null);
        if (null != baseState) {
            if (baseState instanceof UserRegistrationState) {
                uiView.showActionBar();
            }
            baseState.setPresenter(this);
            appFrameworkApplication.getFlowManager().setCurrentState(baseState);
            baseState.navigate(fragmentLauncher);
        }
    }

}
