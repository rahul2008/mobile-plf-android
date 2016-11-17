/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.introscreen.WelcomeView;
import com.philips.platform.baseapp.screens.userregistration.URStateListener;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Spalsh presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UIBasePresenter implements URStateListener {
    private final WelcomeView uiView;
    private String APP_START = "onAppStartEvent";
    private BaseState baseState;
    private FragmentLauncher fragmentLauncher;

    public SplashPresenter(WelcomeView uiView) {
        super(uiView);
        this.uiView = uiView;
        setState(AppStates.SPLASH);
    }

    /**
     * The methods takes decision to load which next state needs to be loaded after splash screen
     * Depending upon the User registration is compelted on not state will change
     *
     */
    @Override
    public void onEvent(int componentID) {
        baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.SPLASH, APP_START);
        final FragmentLauncher fragmentLauncher = new FragmentLauncher(uiView.getFragmentActivity(), uiView.getContainerId(), null);
        baseState.setPresenter(this);
        if (null != baseState) {
            if (baseState instanceof UserRegistrationState) {
                ((UserRegistrationState) baseState).registerUIStateListener(this);
                uiView.showActionBar();
            }
            baseState.navigate(fragmentLauncher);
        }
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailure() {

    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(uiView.getFragmentActivity(), uiView.getContainerId(), uiView.getActionBarListener());
        return fragmentLauncher;
    }
    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) uiView.getFragmentActivity().getApplicationContext();
    }
    @Override
    public void onStateComplete(BaseState baseState) {
        this.baseState = getApplicationContext().getTargetFlowManager().getNextState(AppStates.SPLASH, "splash_home");
        if (null != this.baseState) {
            fragmentLauncher = getFragmentLauncher();
            this.baseState.setPresenter(this);
            finishActivity();
            this.baseState.navigate(fragmentLauncher);
        }
    }
    protected void finishActivity() {
        uiView.finishActivityAffinity();
    }
}
