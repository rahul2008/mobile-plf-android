/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.AppframeworkUtility;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.URStateListener;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Settings presenter handles the state change for launching UR or IAP from on click of buttons
 *
 */
public class SettingsFragmentPresenter extends UIBasePresenter implements URStateListener {

    private static final int USER_REGISTRATION_STATE = 999;
    private static final int HOME_ACTIVITY_STATE = 998;
    private final SettingsView settingsView;
    private AppFrameworkApplication appFrameworkApplication;
    private UIState uiState;
    private FragmentLauncher fragmentLauncher;

    public SettingsFragmentPresenter(final SettingsView settingsView) {
        super(settingsView);
        this.settingsView = settingsView;
        setState(UIState.UI_SETTINGS_FRAGMENT_STATE);
    }

    /**
     * Handles the click events for Login / Log out button
     * Launches UR for Login button click and Logs out of UR for logout button
     * Launches IAP history on click of Order history ( only if user is logged in )
     *  @param componentID takes component Id
     *
     *
     */
    @Override
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
        uiState = getUiState(componentID);
        uiState.setPresenter(this);
        fragmentLauncher = getFragmentLauncher();
        appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
    }

    protected UIState getUiState(final int componentID) {
        switch (componentID){
            case Constants.LOGOUT_BUTTON_CLICK_CONSTANT:
                uiState = new HomeFragmentState();
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                uiState.setUiStateData(homeStateData);
                break;
            case Constants.IAP_PURCHASE_HISTORY:
                uiState = new IAPState();
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_PURCHASE_HISTORY_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(settingsView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                uiState.setUiStateData(uiStateDataModel);
                break;
            case USER_REGISTRATION_STATE:
                uiState = new UserRegistrationState();
                break;
            case HOME_ACTIVITY_STATE:
                uiState = new HomeActivityState();
                break;
        }
        return uiState;
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(settingsView.getFragmentActivity(), settingsView.getContainerId(), settingsView.getActionBarListener());
        return fragmentLauncher;
    }

    /**
     * Laods the User registration
     */
    @Override
    public void onLoad() {
        appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
        uiState = getUiState(USER_REGISTRATION_STATE);
        fragmentLauncher = getFragmentLauncher();
        uiState.setPresenter(this);
        ((UserRegistrationState)uiState).registerUIStateListener(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState, this.fragmentLauncher);
    }

    /**
     * For setting the next state
     * @param uiState
     */
    @Override
    public void onStateComplete(UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
        this.uiState = getUiState(HOME_ACTIVITY_STATE);
        fragmentLauncher = getFragmentLauncher();
        this.uiState.setPresenter(this);
        settingsView.finishActivityAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {
        if (AppframeworkUtility.isActivityAlive(settingsView.getFragmentActivity())) {
            ((AppFrameworkBaseActivity) settingsView.getFragmentActivity()).setCartItemCount(0);
            appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
            uiState = getUiState(Constants.LOGOUT_BUTTON_CLICK_CONSTANT);
            fragmentLauncher = getFragmentLauncher();
            uiState.setPresenter(this);
            appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
        }
    }

    @Override
    public void onLogoutFailure() {

    }
}
