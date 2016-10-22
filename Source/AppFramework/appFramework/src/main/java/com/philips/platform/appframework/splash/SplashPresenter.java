/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.splash;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIView;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Spalsh presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UIBasePresenter {
    private final UIView uiView;
    private UIState uiState;
    private UserRegistrationState userRegistrationState;
    private FragmentLauncher fragmentLauncher;

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
        userRegistrationState = new UserRegistrationState();
        FlowManager targetFlowManager = new FlowManager();
        final AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) uiView.getFragmentActivity().getApplicationContext();
//        uiState = FlowManagerJson.getInstance(appFrameworkApplication).getNextState(AppStates.SPLASH);
        if (userRegistrationState.getUserObject(uiView.getFragmentActivity()).isUserSignIn()) {
            uiState = targetFlowManager.getState("splash_navigate_home");
        } else {
            uiState = targetFlowManager.getState("splash_navigate_welcome");
        }
        uiState.setPresenter(this);
        fragmentLauncher = new FragmentLauncher(uiView.getFragmentActivity(), R.id.welcome_frame_container, null);
        uiState.navigate(fragmentLauncher);
    }

}
