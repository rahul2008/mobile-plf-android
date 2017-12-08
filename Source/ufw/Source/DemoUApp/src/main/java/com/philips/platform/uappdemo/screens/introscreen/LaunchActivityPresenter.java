/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.introscreen;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.flowmanager.base.UIStateListener;
import com.philips.platform.flowmanager.utility.UappConstants;
import com.philips.platform.uappdemo.screens.base.UappBasePresenter;
import com.philips.platform.uappdemo.screens.splash.SplashState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


/**
 * Welcome presenter handles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class LaunchActivityPresenter extends UappBasePresenter implements UIStateListener {

    public static final int APP_LAUNCH_STATE = 890;
    private UappLaunchView launchView;
    private FragmentLauncher fragmentLauncher;
    private String LAUNCH_BACK_PRESSED = "back";
    private String APP_LAUNCH = "onAppLaunch";

    public LaunchActivityPresenter(UappLaunchView launchView) {
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
        BaseFlowManager targetFlowManager = launchView.getTargetFlowManager();
        BaseState baseState = null;
        if (event.equals(APP_LAUNCH))
            baseState = getSplashState();
        else if (event.equals(LAUNCH_BACK_PRESSED))
            baseState = targetFlowManager.getBackState(targetFlowManager.getCurrentState());

        if (baseState != null) {
            baseState.setUiStateData(getUiStateData());
            baseState.navigate(fragmentLauncher);
        }
    }

    @NonNull
    protected SplashState getSplashState() {
        return new SplashState();
    }

    protected void showActionBar() {
        launchView.showActionBar();
    }

    // TODO: Deepthi  can we make it abstract if its common
    protected String getEvent(final int componentID) {
        switch (componentID) {
            case UappConstants.BACK_BUTTON_CLICK_CONSTANT:
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
        homeStateData.setFragmentLaunchType(UappConstants.ADD_HOME_FRAGMENT);
        return homeStateData;
    }

    @Override
    public void onStateComplete(BaseState baseState) {

    }
}
