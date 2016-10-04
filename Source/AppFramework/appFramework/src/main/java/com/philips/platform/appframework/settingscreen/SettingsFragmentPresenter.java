/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Settings presenter handles the state change for launching UR or IAP from on click of buttons
 *
 */
public class SettingsFragmentPresenter extends UIBasePresenter implements UIStateListener {


    private AppFrameworkApplication appFrameworkApplication;
    private UIState uiState;
    private Context activityContext;
    private FragmentLauncher fragmentLauncher;

    SettingsFragmentPresenter(){
        setState(UIState.UI_SETTINGS_FRAGMENT_STATE);
    }

    /**
     * Handles the click events for Login / Log out button
     * Launches UR for Login button click and Logs out of UR for logout button
     * Launches IAP history on click of Order history ( only if user is logged in )
     *
     * @param componentID takes component Id
     *
     * @param context needs context
     */
    @Override
    public void onClick(int componentID, Context context) {
        activityContext = context;
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        switch (componentID){
            case Constants.LOGOUT_BUTTON_CLICK_CONSTANT:
                uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                uiState.setUiStateData(homeStateData);
                fragmentLauncher = new FragmentLauncher((AppFrameworkBaseActivity)context, R.id.frame_container,(AppFrameworkBaseActivity)context);
                uiState.setPresenter(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
                break;
            case Constants.IAP_PURCHASE_HISTORY:
                uiState = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                fragmentLauncher = new FragmentLauncher((AppFrameworkBaseActivity)context, R.id.frame_container,(AppFrameworkBaseActivity)context);
                IAPState.InAppStateData uiStateDataModel = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE).new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_PURCHASE_HISTORY_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                uiState.setUiStateData(uiStateDataModel);
                uiState.setPresenter(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
                break;
        }
    }

    /**
     * Laods the User registration
     * @param context requires context
     */
    @Override
    public void onLoad(Context context) {
        activityContext = context;
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        fragmentLauncher = new FragmentLauncher((AppFrameworkBaseActivity)context,R.id.frame_container,(AppFrameworkBaseActivity)context);
        uiState.setPresenter(this);
        ((UserRegistrationState)uiState).registerUIStateListener(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
    }

    /**
     * For setting the next state
     * @param uiState
     */
    @Override
    public void onStateComplete(UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) activityContext.getApplicationContext();
        this.uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        fragmentLauncher = new FragmentLauncher((AppFrameworkBaseActivity)activityContext,R.id.frame_container,(AppFrameworkBaseActivity)activityContext);
        this.uiState.setPresenter(this);
        ((AppFrameworkBaseActivity)activityContext).finishAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState,fragmentLauncher);
    }
}
