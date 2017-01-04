/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.settingscreen;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import philips.appframeworklibrary.flowmanager.base.BaseFlowManager;
import philips.appframeworklibrary.flowmanager.base.BaseState;
import philips.appframeworklibrary.flowmanager.exceptions.NoEventFoundException;

/**
 * Settings presenter handles the state change for launching UR or IAP from on click of buttons
 *
 */
public class SettingsFragmentPresenter extends UIBasePresenter{

    private static final int USER_REGISTRATION_STATE = 999;
    private static final int HOME_ACTIVITY_STATE = 998;
    private static final String SETTINGS_LOGIN = "login";
    private final SettingsView settingsView;
    private BaseState baseState;
    private FragmentLauncher fragmentLauncher;
    private String SETTINGS_REGISTRATION = "settings_registration";
    private String SETTINGS_LOGOUT = "logout";
    private String SETTINGS_ORDER_HISTORY = "order_history";

    public SettingsFragmentPresenter(final SettingsView settingsView) {
        super(settingsView);
        this.settingsView = settingsView;
        setState(AppStates.SETTINGS);
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
    public void onEvent(int componentID) {
        final BaseState.UIStateData uiStateData = setStateData(componentID);
        String eventState = getEventState(componentID);
        if (settingsView != null) {
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            try {
                baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), eventState);
            } catch (NoEventFoundException e) {
                e.printStackTrace();
            }
        }
        if (baseState != null) {
            baseState.setUiStateData(uiStateData);
            fragmentLauncher = getFragmentLauncher();
            baseState.navigate(fragmentLauncher);
        }
    }

    protected BaseState.UIStateData setStateData(final int componentID) {
        switch (componentID){
            case Constants.LOGOUT_BUTTON_CLICK_CONSTANT:
                BaseState.UIStateData homeStateData = new BaseState.UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
            // Commented the order history/purchase history code.
            /*case Constants.IAP_PURCHASE_HISTORY:
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_PURCHASE_HISTORY_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(settingsView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                return uiStateDataModel;*/
        }
        return null;
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(settingsView.getFragmentActivity(), settingsView.getContainerId(), settingsView.getActionBarListener());
        return fragmentLauncher;
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
    }


    protected String getEventState(final int componentID) {
        switch (componentID) {
            case Constants.LOGOUT_BUTTON_CLICK_CONSTANT:
                return SETTINGS_LOGOUT;
            case Constants.IAP_PURCHASE_HISTORY:
                return SETTINGS_ORDER_HISTORY;
            case Constants.LOGIN_BUTTON_CLICK_CONSTANT:
                return SETTINGS_LOGIN;
        }
        return null;
    }
}
