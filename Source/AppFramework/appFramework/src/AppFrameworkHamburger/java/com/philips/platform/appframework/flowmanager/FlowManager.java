/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.baseapp.condition.ConditionAppLaunch;
import com.philips.platform.baseapp.condition.ConditionIsDonePressed;
import com.philips.platform.baseapp.condition.ConditionIsLoggedIn;
import com.philips.platform.baseapp.screens.aboutscreen.AboutScreenState;
import com.philips.platform.baseapp.screens.consumercare.SupportFragmentState;
import com.philips.platform.baseapp.screens.dataservices.DataServicesState;
import com.philips.platform.baseapp.screens.debugtest.DebugTestFragmentState;
import com.philips.platform.baseapp.screens.homefragment.HomeFragmentState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.introscreen.welcomefragment.WelcomeState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.settingscreen.SettingsFragmentState;
import com.philips.platform.baseapp.screens.splash.SplashState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.modularui.stateimpl.ConnectivityFragmentState;

import java.util.Map;

import philips.appframeworklibrary.flowmanager.base.BaseCondition;
import philips.appframeworklibrary.flowmanager.base.BaseFlowManager;
import philips.appframeworklibrary.flowmanager.base.BaseState;

public class FlowManager extends BaseFlowManager {

    public FlowManager(Context context, String jsonPath) {
        super(context, jsonPath);
    }

    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        uiStateMap.put(AppStates.WELCOME, new WelcomeState());
        uiStateMap.put(AppStates.ON_BOARDING_REGISTRATION, new UserRegistrationOnBoardingState());
        uiStateMap.put(AppStates.SETTINGS_REGISTRATION, new UserRegistrationSettingsState());
        uiStateMap.put(AppStates.HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(AppStates.HAMBURGER_HOME, new HamburgerActivityState());
        uiStateMap.put(AppStates.ABOUT, new AboutScreenState());
        uiStateMap.put(AppStates.DEBUG, new DebugTestFragmentState());
        uiStateMap.put(AppStates.SETTINGS, new SettingsFragmentState());
        uiStateMap.put(AppStates.IAP, new IAPRetailerFlowState());
        uiStateMap.put(AppStates.PR, new ProductRegistrationState());
        uiStateMap.put(AppStates.SUPPORT, new SupportFragmentState());
        uiStateMap.put(AppStates.SPLASH, new SplashState());
        uiStateMap.put(AppStates.DATA_SYNC, new DataServicesState());
        uiStateMap.put(AppStates.CONNECTIVITY,new ConnectivityFragmentState());
    }

    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }
}
