/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.settingscreen;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIStateData;
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
    private static final String SETTINGS_LOGIN = "login";
    private final SettingsView settingsView;
    private AppFrameworkApplication appFrameworkApplication;
    private BaseState baseState;
    private FragmentLauncher fragmentLauncher;
    private String SETTINGS_REGISTRATION = "settings_registration";
    private String SETTINGS_LOGOUT = "logout";
    private String SETTINGS_ORDER_HISTORY = "order_history";

    public SettingsFragmentPresenter(final SettingsView settingsView) {
        super(settingsView);
        this.settingsView = settingsView;
        setState(BaseState.UI_SETTINGS_FRAGMENT_STATE);
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
        final UIStateData uiStateData = setStateData(componentID);
        String eventState = getEventState(componentID);
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.SETTINGS, eventState);
        if (baseState != null) {
            baseState.setPresenter(this);
            baseState.setUiStateData(uiStateData);
            if (eventState.equals(SETTINGS_LOGIN)) {
                ((UserRegistrationState) baseState).registerUIStateListener(this);
            }
            fragmentLauncher = getFragmentLauncher();
            baseState.navigate(fragmentLauncher);
        }
    }

    protected UIStateData setStateData(final int componentID) {
        switch (componentID){
            case Constants.LOGOUT_BUTTON_CLICK_CONSTANT:
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
            case Constants.IAP_PURCHASE_HISTORY:
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_PURCHASE_HISTORY_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(settingsView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                return uiStateDataModel;
        }
        return null;
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

    }

    /**
     * For setting the next state
     * @param baseState
     */
    @Override
    public void onStateComplete(BaseState baseState) {
        appFrameworkApplication = (AppFrameworkApplication) settingsView.getFragmentActivity().getApplicationContext();
        this.baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.SETTINGS, SETTINGS_REGISTRATION);
        fragmentLauncher = getFragmentLauncher();
        this.baseState.setPresenter(this);
        settingsView.finishActivityAffinity();
        this.baseState.navigate(fragmentLauncher);
    }

    @Override
    public void onLogoutSuccess() {
        final FragmentActivity fragmentActivity = settingsView.getFragmentActivity();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            ((AppFrameworkBaseActivity) fragmentActivity).setCartItemCount(0);
            appFrameworkApplication = (AppFrameworkApplication) fragmentActivity.getApplicationContext();
            baseState = appFrameworkApplication.getTargetFlowManager().getNextState(BaseAppState.SETTINGS, SETTINGS_LOGOUT);
            fragmentLauncher = getFragmentLauncher();
            baseState.setPresenter(this);
            baseState.navigate(fragmentLauncher);
        }
    }

    @Override
    public void onLogoutFailure() {

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
