/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen;

import android.support.annotation.NonNull;

import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.splash.SplashState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import philips.appframeworklibrary.flowmanager.base.BaseFlowManager;
import philips.appframeworklibrary.flowmanager.base.BaseState;
import philips.appframeworklibrary.flowmanager.base.UIStateData;
import philips.appframeworklibrary.flowmanager.base.UIStateListener;

/**
 * Welcome presenter handles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class LaunchActivityPresenter extends UIBasePresenter implements UIStateListener{

    public static final int APP_LAUNCH_STATE = 890;
    private LaunchView launchView;
    private FragmentLauncher fragmentLauncher;
    private String LAUNCH_BACK_PRESSED = "back";
    private String APP_LAUNCH = "onAppLaunch";

    public LaunchActivityPresenter(LaunchView launchView) {
        super(launchView);
        this.launchView = launchView;
    }

    /**
     * Handles the onclick of Welcome Skip and Done button
     *
     * @param componentID : takes compenent Id
     */
    @Override
    public void onEvent(int componentID) {
        showActionBar();
        String event = getEvent(componentID);
        fragmentLauncher = getFragmentLauncher();
        BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
        BaseState baseState = null;
        if (event.equals(APP_LAUNCH))
            baseState = new SplashState();
        else if (event.equals(LAUNCH_BACK_PRESSED))
            baseState = targetFlowManager.getBackState();

        if (baseState != null) {
            baseState.setUiStateData(getUiStateData());
            baseState.navigate(fragmentLauncher);
        }
    }

    protected void showActionBar() {
        launchView.showActionBar();
    }

    // TODO: Deepthi  can we make it abstract if its common
    protected String getEvent(final int componentID) {
        switch (componentID) {
            case Constants.BACK_BUTTON_CLICK_CONSTANT:
                return LAUNCH_BACK_PRESSED;
            default:
                return APP_LAUNCH;
        }
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(launchView.getFragmentActivity(), launchView.getContainerId(), launchView.getActionBarListener());
        return fragmentLauncher;
    }

    @NonNull
    protected UIStateData getUiStateData() {
       UIStateData homeStateData = new UIStateData();
        homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
        return homeStateData;
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) launchView.getFragmentActivity().getApplicationContext();
    }

    @Override
    public void onStateComplete(BaseState baseState) {

    }
}
