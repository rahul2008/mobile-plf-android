/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.splash;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateListener;
import com.philips.platform.flowmanager.UappStates;
import com.philips.platform.uappdemo.screens.base.UappBasePresenter;
import com.philips.platform.uappdemo.screens.introscreen.UappLaunchView;
import com.philips.platform.uappdemo.screens.stateimpl.HamburgerActivityState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


/**
 * Splash presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UappBasePresenter implements UIStateListener {
    private final UappLaunchView uiView;
    private String APP_START = "onSplashTimeOut";

    public SplashPresenter(UappLaunchView uiView) {
        super(uiView);
        this.uiView = uiView;
        setState(UappStates.SPLASH);
    }

    /**
     * The methods takes decision to load which next state needs to be loaded after splash screen
     * Depending upon the User registration is compelted on not state will change
     *
     */
    @Override
    public void onEvent(int componentID) {
        BaseFlowManager targetFlowManager = uiView.getTargetFlowManager();
        final BaseState splashState = targetFlowManager.getState(UappStates.SPLASH);
        BaseState baseState = targetFlowManager.getNextState(splashState, APP_START);
        if (null != baseState) {
            baseState.setStateListener(this);
            baseState.navigate(getFragmentLauncher());
            if (baseState instanceof HamburgerActivityState)
                finishActivity();
        }
    }

    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(uiView.getFragmentActivity(), uiView.getContainerId(), uiView.getActionBarListener());
    }
    protected void finishActivity() {
        uiView.finishActivity();
    }

    @Override
    public void onStateComplete(BaseState baseState) {

    }
}
