/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen;

import androidx.annotation.NonNull;

import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.flowmanager.base.UIStateListener;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.splash.SplashState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Welcome presenter handles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class LaunchActivityPresenter extends AbstractUIBasePresenter implements UIStateListener{
    public static final String TAG =  LaunchActivityPresenter.class.getSimpleName();

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
        RALog.e(TAG, " onEvent called ");

        showActionBar();
        String event = getEvent(componentID);
        fragmentLauncher = getFragmentLauncher();
        BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
        BaseState baseState = null;
        try {
            if (event.equals(APP_LAUNCH))
                baseState = getSplashState();
            else if (event.equals(LAUNCH_BACK_PRESSED))
                baseState = targetFlowManager.getBackState(targetFlowManager.getCurrentState());

            if (baseState != null) {
                baseState.setUiStateData(setStateData(baseState.getStateID()));
                baseState.navigate(fragmentLauncher);
                fragmentLauncher = null;
            }
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.e(TAG, e.getMessage());
            Toast.makeText(launchView.getFragmentActivity(), launchView.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
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
        RALog.e(TAG, " getEvent called ");
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
