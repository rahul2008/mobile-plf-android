package com.philips.platform.appframework.flowmanager;

import android.content.Context;
import android.support.annotation.StringDef;

import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class FlowManager extends BaseUiFlowManager {

    /**
     * Each state has a string value defined.
     */
    public static final String WELCOME = "welcome";
    public static final String SUPPORT = "support";
    public static final String SPLASH = "splash";
    public static final String REGISTRATION = "registration";
    public static final String HOME_FRAGMENT = "home_fragment";
    public static final String ABOUT = "about";
    public static final String DEBUG = "debug";
    public static final String IAP = "iap";
    public static final String PR = "pr";
    public static final String SETTINGS = "settings";
    public static final String DATA_SYNC = "data_sync";
    public static final String HAMBURGER_HOME = "home";

    /**
     * Each condition has a string value defined.
     */
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String IS_DONE_PRESSED = "isDonePressed";
    public static final String CONDITION_APP_LAUNCH = "conditionAppLaunch";

    /**
     * This class defines constants for each state ,
     * Any new state should be added here and its constant should be defined here
     * Constants for each state is defined in the BaseAppState class as static strings.
     */

    @StringDef({ABOUT, DEBUG, HOME_FRAGMENT, IAP, PR, REGISTRATION, SETTINGS,
             SPLASH, SUPPORT, WELCOME, DATA_SYNC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppState {}


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
    public void populateStateMap(final Map<String, BaseState> uiStateMap) {
        uiStateMap.put(WELCOME, new WelcomeState());
        uiStateMap.put(REGISTRATION, new UserRegistrationState());
        uiStateMap.put(HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(HAMBURGER_HOME, new HamburgerActivityState());
        uiStateMap.put(ABOUT, new AboutScreenState());
        uiStateMap.put(DEBUG, new DebugTestFragmentState());
        uiStateMap.put(SETTINGS, new SettingsFragmentState());
        uiStateMap.put(IAP, new IAPState());
        uiStateMap.put(PR, new ProductRegistrationState());
        uiStateMap.put(SUPPORT, new SupportFragmentState());
        uiStateMap.put(SPLASH, new SplashState());
        uiStateMap.put(DATA_SYNC, new DataSyncScreenState());
    }

    public void populateConditionMap(final Map<String, BaseCondition> baseConditionMap) {
        baseConditionMap.put(IS_LOGGED_IN, new ConditionIsLoggedIn());
        baseConditionMap.put(IS_DONE_PRESSED, new ConditionIsDonePressed());
        baseConditionMap.put(CONDITION_APP_LAUNCH, new ConditionAppLaunch());
    }
}
