/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.introscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.CoCoListener;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.util.UIConstants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Welcome presenter hanles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class WelcomePresenter extends UIBasePresenter implements CoCoListener {

    public WelcomePresenter() {

    }

    AppFrameworkApplication appFrameworkApplication;
    SharedPreferenceUtility sharedPreferenceUtility;
    UIState uiState;

    /**
     * Handles the onclick of Welcome Skip and Done button
     * @param componentID : takes compenent Id
     * @param context : takes context
     */
    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        ((WelcomeActivity) context).changeActionBarState(true);
        switch (componentID) {

            case R.id.appframework_skip_button:
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                uiState.init(new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context));
                uiState.setPresenter(this);
                ((UserRegistrationState)uiState).registerForNextState(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
                break;
            case R.id.start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(context);
                sharedPreferenceUtility.writePreferenceBoolean(UIConstants.DONE_PRESSED, true);
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                uiState.init(new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context));
                uiState.setPresenter(this);
                ((UserRegistrationState)uiState).registerForNextState(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
                break;
            case WelcomeActivity.backButtonClick:
                uiState = new HomeActivityState(UIState.UI_HOME_STATE);
                appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
                if(appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == (UIState.UI_USER_REGISTRATION_STATE))
                {
                    ((WelcomeActivity) context).finishAffinity();
                    uiState.setPresenter(this);
                    appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
                }
                break;
        }
    }

    /**
     * Takes care of handling whether to show user regitration after the splash screen has loaded or to show Welcome fragments if onboarding was skipped at the time of first launch
     * @param context
     */
    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
        if (appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_WELCOME_REGISTRATION_STATE || appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_USER_REGISTRATION_STATE) {
            ((WelcomeActivity) context).changeActionBarState(true);
            setState(UIState.UI_WELCOME_REGISTRATION_STATE);
            uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
            uiState.init(new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context));
            uiState.setPresenter(this);
            ((UserRegistrationState)uiState).registerForNextState(this);
            appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
        } else {
            setState(UIState.UI_WELCOME_STATE);
            appFrameworkApplication.getFlowManager().getCurrentState().setStateID(UIState.UI_WELCOME_STATE);
            ((WelcomeActivity) context).changeActionBarState(false);
            ((WelcomeActivity) context).loadWelcomeFragment();
        }

    }

    @Override
    public void coCoCallBack(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        uiState.setPresenter(this);
        ((WelcomeActivity) context).finishAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
    }
}
