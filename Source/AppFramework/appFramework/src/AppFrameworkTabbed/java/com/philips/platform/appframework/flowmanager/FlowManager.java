package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.flowmanager.condition.AppConditions;
import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.flowmanager.condition.ConditionIsLoggedIn;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.HomeTabbedActivityState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SplashState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.stateimpl.WelcomeState;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class FlowManager extends BaseUiFlowManager {

    private static FlowManager flowManager;

    private FlowManager(Context context, int jsonPath) {
        super(context, jsonPath);
    }

    public static FlowManager getInstance(Context context, int jsonPath) {
        if (flowManager == null)
            flowManager = new FlowManager(context, jsonPath);

        return flowManager;
    }

    @Override
    public void addStateMap(final Map<AppStates, UIState> appStatesUIStateMap) {
        appStatesUIStateMap.put(AppStates.WELCOME, new WelcomeState());
        appStatesUIStateMap.put(AppStates.REGISTRATION, new UserRegistrationState());
        appStatesUIStateMap.put(AppStates.HOMEFRAGMENT, new HomeFragmentState());
        appStatesUIStateMap.put(AppStates.HOME, new HomeTabbedActivityState());
        appStatesUIStateMap.put(AppStates.ABOUT, new AboutScreenState());
        appStatesUIStateMap.put(AppStates.DEBUG, new DebugTestFragmentState());
        appStatesUIStateMap.put(AppStates.SETTINGS, new SettingsFragmentState());
        appStatesUIStateMap.put(AppStates.IAP, new IAPState());
        appStatesUIStateMap.put(AppStates.PR, new ProductRegistrationState());
        appStatesUIStateMap.put(AppStates.SUPPORT, new SupportFragmentState());
        appStatesUIStateMap.put(AppStates.SPLASH, new SplashState());
    }

    @Override
    public void addConditionMap(final Map<AppConditions, BaseCondition> appStatesConditionMap) {
        appStatesConditionMap.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        appStatesConditionMap.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
    }

}
