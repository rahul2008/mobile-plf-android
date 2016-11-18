package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.BaseUiFlowManager;
import com.philips.platform.appframework.stateimpl.HomeTabbedActivityState;
import com.philips.platform.baseapp.condition.ConditionAppLaunch;
import com.philips.platform.baseapp.condition.ConditionIsDonePressed;
import com.philips.platform.baseapp.condition.ConditionIsLoggedIn;
import com.philips.platform.baseapp.screens.aboutscreen.AboutScreenState;
import com.philips.platform.baseapp.screens.consumercare.SupportFragmentState;
import com.philips.platform.baseapp.screens.datasevices.temperature.DataSyncScreenState;
import com.philips.platform.baseapp.screens.debugtest.DebugTestFragmentState;
import com.philips.platform.baseapp.screens.homefragment.HomeFragmentState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.introscreen.welcomefragment.WelcomeState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.settingscreen.SettingsFragmentState;
import com.philips.platform.baseapp.screens.splash.SplashState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSettingsState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationSplashState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationWelcomeState;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class FlowManager extends BaseUiFlowManager {
    private static FlowManager flowManager = null;

    // TODO: Deepthi to support download from cloud  ?
    private FlowManager(Context context, String jsonPath) {
        super(context, jsonPath);
    }

    public static synchronized FlowManager getInstance(Context context, String jsonPath) {
        if (flowManager == null) {
            synchronized (FlowManager.class) {
                if (flowManager == null)
                    flowManager = new FlowManager(context, jsonPath);
            }
        }
        return flowManager;
    }

    @Override
    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        uiStateMap.put(AppStates.WELCOME, new WelcomeState());
        uiStateMap.put(AppStates.SPLASH_REGISTRATION, new UserRegistrationSplashState());
        uiStateMap.put(AppStates.WELCOME_REGISTRATION, new UserRegistrationWelcomeState());
        uiStateMap.put(AppStates.SETTINGS_REGISTRATION, new UserRegistrationSettingsState());
        uiStateMap.put(AppStates.HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(AppStates.TAB_HOME, new HomeTabbedActivityState());
        uiStateMap.put(AppStates.ABOUT, new AboutScreenState());
        uiStateMap.put(AppStates.DEBUG, new DebugTestFragmentState());
        uiStateMap.put(AppStates.SETTINGS, new SettingsFragmentState());
        uiStateMap.put(AppStates.IAP, new IAPRetailerFlowState());
        uiStateMap.put(AppStates.PR, new ProductRegistrationState());
        uiStateMap.put(AppStates.SUPPORT, new SupportFragmentState());
        uiStateMap.put(AppStates.SPLASH, new SplashState());
        uiStateMap.put(AppStates.DATA_SYNC, new DataSyncScreenState());
    }

    @Override
    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(AppConditions.CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }

}
