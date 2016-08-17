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
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.util.UIConstants;

public class WelcomePresenter extends UIBasePresenter implements UserRegistrationState.SetStateCallBack {

    public WelcomePresenter() {

    }

    AppFrameworkApplication appFrameworkApplication;
    SharedPreferenceUtility sharedPreferenceUtility;
    UIState uiState;

    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        ((WelcomeActivity) context).changeActionBarState(true);
        switch (componentID) {

            case R.id.appframework_skip_button:
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                uiState.setPresenter(this);
                ((UserRegistrationState)uiState).registerForNextState(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
                break;
            case R.id.start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(context);
                sharedPreferenceUtility.writePreferenceBoolean(UIConstants.DONE_PRESSED, true);
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
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

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
        if (appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_WELCOME_REGISTRATION_STATE || appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_USER_REGISTRATION_STATE) {
            ((WelcomeActivity) context).changeActionBarState(true);
            setState(UIState.UI_WELCOME_REGISTRATION_STATE);
            uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
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
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        uiState.setPresenter(this);
        ((WelcomeActivity) context).finishAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
    }

    @Override
    public void updateTitle(int titleResourceID,Context context) {
        ((WelcomeActivity) context).updateTitle();
    }

    @Override
    public void updateTitleWithBack(int titleResourceID,Context context) {
        ((WelcomeActivity) context).updateTitleWithBack();
    }

    @Override
    public void updateTitleWIthoutBack(int titleResourceID,Context context) {
        ((WelcomeActivity) context).updateTitleWithoutBack();
    }
}
