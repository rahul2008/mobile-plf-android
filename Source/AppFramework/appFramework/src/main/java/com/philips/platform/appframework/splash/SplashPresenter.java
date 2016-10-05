/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.splash;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIView;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.stateimpl.WelcomeState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Spalsh presenter loads the splash screen and sets the next state after splash
 * The wait timer for splash screen is 3 secs ( configurable by verticals)
 */
public class SplashPresenter extends UIBasePresenter {
    private final UIView uiView;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private AppFrameworkApplication appFrameworkApplication;
    private UIState uiState;
    private UserRegistrationState userRegistrationState;
    private FragmentLauncher fragmentLauncher;

    public SplashPresenter(final UIView uiView) {
        super(uiView);
        this.uiView = uiView;
        setState(UIState.UI_SPLASH_STATE);
    }

    @Override
    public void onClick(int componentID, Context context) {

    }

    /**
     * The methods takes decision to load which next state needs to be loaded after splash screen
     * Depending upon the User registration is compelted on not state will change
     *
     * @param context
     */
    @Override
    public void onLoad(Context context) {
        sharedPreferenceUtility = getSharedPreferenceUtility(context);
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        userRegistrationState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        if (userRegistrationState.getUserObject(context).isUserSignIn()) {
            uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        } else {
            uiState = new WelcomeState(UIState.UI_WELCOME_STATE);
        }
        uiState.setPresenter(this);
        fragmentLauncher = new FragmentLauncher(uiView.getFragmentActivity(), R.id.welcome_frame_container, null);
        appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
    }

    public SharedPreferenceUtility getSharedPreferenceUtility(Context context) {
        return new SharedPreferenceUtility(context);
    }
}
