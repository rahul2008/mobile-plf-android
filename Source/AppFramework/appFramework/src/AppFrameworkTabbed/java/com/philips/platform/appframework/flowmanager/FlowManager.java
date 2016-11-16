package com.philips.platform.appframework.flowmanager;

import android.content.Context;

import com.philips.platform.appframework.stateimpl.HomeTabbedActivityState;
import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.condition.ConditionAppLaunch;
import com.philips.platform.flowmanager.condition.ConditionIsDonePressed;
import com.philips.platform.flowmanager.condition.ConditionIsLoggedIn;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DataSyncScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
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

    private static FlowManager flowManager = null;

    private FlowManager(Context context, int jsonPath) {
        super(context, jsonPath);
    }

    public static synchronized FlowManager getInstance(Context context, int jsonPath) {
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
        uiStateMap.put(AppStates.REGISTRATION, new UserRegistrationState());
        uiStateMap.put(AppStates.HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(AppStates.TAB_HOME, new HomeTabbedActivityState());
        uiStateMap.put(AppStates.ABOUT, new AboutScreenState());
        uiStateMap.put(AppStates.DEBUG, new DebugTestFragmentState());
        uiStateMap.put(AppStates.SETTINGS, new SettingsFragmentState());
        uiStateMap.put(AppStates.IAP, new IAPState());
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
