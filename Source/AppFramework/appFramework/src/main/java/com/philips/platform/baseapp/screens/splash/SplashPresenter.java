/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.introscreen.LaunchView;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Spalsh presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UIBasePresenter{
    private final LaunchView uiView;
    private String APP_START = "onSplashTimeOut";
    private BaseState baseState;
    private FragmentLauncher fragmentLauncher;

    public SplashPresenter(LaunchView uiView) {
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
        BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
        baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.SPLASH), APP_START);
        baseState.setStateListener(this);
        if (null != baseState) {
            if (baseState instanceof UserRegistrationState) {
                uiView.showActionBar();
            }
            baseState.navigate(getFragmentLauncher());
        }
    }

    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(uiView.getFragmentActivity(), uiView.getContainerId(), uiView.getActionBarListener());
    }
    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) uiView.getFragmentActivity().getApplicationContext();
    }
    protected void finishActivity() {
        uiView.finishActivityAffinity();
    }
}
