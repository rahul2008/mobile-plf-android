/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.splash;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.jsonstates.EventStates;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIView;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Spalsh presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UIBasePresenter {
    private final UIView uiView;

    public SplashPresenter(final UIView uiView) {
        super(uiView);
        this.uiView = uiView;
        setState(UIState.UI_SPLASH_STATE);
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
        final UIState uiState = FlowManager.getInstance(appFrameworkApplication).getNextState(AppStates.SPLASH, EventStates.APP_START);
        UIStateData homeStateData = new UIStateData();
        homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
        uiState.setUiStateData(homeStateData);
        final FragmentLauncher fragmentLauncher = new FragmentLauncher(uiView.getFragmentActivity(), R.id.welcome_frame_container, null);
        if (null != uiState) {
            uiState.setPresenter(this);
            appFrameworkApplication.getFlowManager().setCurrentState(uiState);
            uiState.navigate(fragmentLauncher);
        }
    }

}
