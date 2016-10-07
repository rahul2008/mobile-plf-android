/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
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
        switch (componentID){
            case Constants.LOGOUT_BUTTON_CLICK_CONSTANT:
                uiState = new HomeFragmentState();
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                uiState.setUiStateData(homeStateData);
                fragmentLauncher = new FragmentLauncher(settingsView.getFragmentActivity(), settingsView.getContainerId(), settingsView.getActionBarListener());
                uiState.setPresenter(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
                break;
            case Constants.IAP_PURCHASE_HISTORY:
                uiState = new IAPState();
                fragmentLauncher = new FragmentLauncher(settingsView.getFragmentActivity(), settingsView.getContainerId(), settingsView.getActionBarListener());
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_PURCHASE_HISTORY_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(settingsView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                uiState.setUiStateData(uiStateDataModel);
                uiState.setPresenter(this);
                appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
                break;
        }
    }

    /**
     * Laods the User registration
     */
    @Override
    public void onLoad() {
        appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
        uiState = new UserRegistrationState();
        fragmentLauncher = new FragmentLauncher(settingsView.getFragmentActivity(), settingsView.getContainerId(), settingsView.getActionBarListener());
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
        appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
        this.uiState = new HomeActivityState();
        fragmentLauncher = new FragmentLauncher(settingsView.getFragmentActivity(), settingsView.getContainerId(), settingsView.getActionBarListener());
        this.uiState.setPresenter(this);
        settingsView.finishActivityAffinity();
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState,fragmentLauncher);
    }
}
