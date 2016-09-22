/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.introscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Welcome presenter handles the events inside welcome fragment
 * it takes care of scenarios in which we can complete onboarding or skip it for time being
 */
public class WelcomePresenter extends UIBasePresenter implements UIStateListener {


    private Context activityContext;
    private AppFrameworkApplication appFrameworkApplication;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private UIState uiState;
    private FragmentLauncher fragmentLauncher;

    public WelcomePresenter() {

    }

    /**
     * Handles the onclick of Welcome Skip and Done button
     * @param componentID : takes compenent Id
     * @param context : takes context
     */
    @Override
    public void onClick(int componentID, Context context) {
        activityContext = context;
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        ((WelcomeActivity) context).changeActionBarState(true);
        switch (componentID) {

            case R.id.appframework_skip_button:
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                fragmentLauncher = new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context);
                uiState.setPresenter(this);
                ((UserRegistrationState)uiState).registerUIStateListener(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
                break;
            case R.id.start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(context);
                sharedPreferenceUtility.writePreferenceBoolean(Constants.DONE_PRESSED, true);
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                fragmentLauncher = new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context);
                uiState.setPresenter(this);
                ((UserRegistrationState)uiState).registerUIStateListener(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
                break;
            case Constants.BACK_BUTTON_CLICK_CONSTANT:
                uiState = new HomeActivityState(UIState.UI_HOME_STATE);
                fragmentLauncher = new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context);
                appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
                if(appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == (UIState.UI_USER_REGISTRATION_STATE))
                {
                    ((WelcomeActivity) context).finishAffinity();
                    uiState.setPresenter(this);
                    appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
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
        activityContext = context;
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
        if (sharedPreferenceUtility.getPreferenceBoolean(Constants.DONE_PRESSED)|| appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_USER_REGISTRATION_STATE) {
            ((WelcomeActivity) context).changeActionBarState(true);
            setState(UIState.UI_WELCOME_REGISTRATION_STATE);
            uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
            fragmentLauncher = new FragmentLauncher((WelcomeActivity)context,R.id.fragment_frame_container,(WelcomeActivity)context);
            uiState.setPresenter(this);
            ((UserRegistrationState)uiState).registerUIStateListener(this);
            appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
        } else {
            setState(UIState.UI_WELCOME_STATE);
            appFrameworkApplication.getFlowManager().getCurrentState().setStateID(UIState.UI_WELCOME_STATE);
            ((WelcomeActivity) context).changeActionBarState(false);
            ((WelcomeActivity) context).loadWelcomeFragment();
        }

    }

    @Override
    public void onStateComplete(UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) activityContext.getApplicationContext();
        this.uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        fragmentLauncher = new FragmentLauncher((WelcomeActivity)activityContext,R.id.fragment_frame_container,(WelcomeActivity)activityContext);
        this.uiState.setPresenter(this);
        ((WelcomeActivity) activityContext).finishAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
    }
}
