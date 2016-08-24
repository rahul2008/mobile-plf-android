/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.InAppPurchaseHistoryFragmentState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;

public class SettingsFragmentPresenter extends UIBasePresenter implements UserRegistrationState.SetStateCallBack {

    SettingsFragmentPresenter(){
        setState(UIState.UI_SETTINGS_FRAGMENT_STATE);
    }

    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;
    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        switch (componentID){
            case SettingsFragment.logOutButton:
                uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                uiState.setPresenter(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
                break;
            case SettingsAdapter.iapHistoryLaunch:
                uiState = new InAppPurchaseHistoryFragmentState(UIState.UI_IAP_SHOPPING_HISTORY_FRAGMENT_STATE);
                uiState.setPresenter(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
                break;
        }
    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        uiState.setPresenter(this);
        ((UserRegistrationState)uiState).registerForNextState(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }

    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        uiState.setPresenter(this);
        ((HomeActivity)context).finishAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }

    @Override
    public void updateTitle(int titleResourceID,Context context) {
        ((HomeActivity)context).updateTitle();
    }

    @Override
    public void updateTitleWithBack(int titleResourceID,Context context) {
        ((HomeActivity)context).updateTitleWithBack();
    }

    @Override
    public void updateTitleWIthoutBack(int titleResourceID,Context context) {
        ((HomeActivity)context).updateTitleWithoutBack();
    }
}
