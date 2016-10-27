package com.philips.platform.appframework.flowmanager;

import com.philips.platform.appframework.stateimpl.HomeTabbedActivityState;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
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

public class TabbedAppState extends BaseAppState {

    public static final String TAB_HOME = "home";

    public TabbedAppState() {
        addMap(stateMap);
    }

    private void addMap(final Map<String, BaseState> uiStateMap) {
        uiStateMap.put(WELCOME, new WelcomeState());
        uiStateMap.put(REGISTRATION, new UserRegistrationState());
        uiStateMap.put(HOME_FRAGMENT, new HomeFragmentState());
        uiStateMap.put(TAB_HOME, new HomeTabbedActivityState());
        uiStateMap.put(ABOUT, new AboutScreenState());
        uiStateMap.put(DEBUG, new DebugTestFragmentState());
        uiStateMap.put(SETTINGS, new SettingsFragmentState());
        uiStateMap.put(IAP, new IAPState());
        uiStateMap.put(PR, new ProductRegistrationState());
        uiStateMap.put(SUPPORT, new SupportFragmentState());
        uiStateMap.put(SPLASH, new SplashState());
    }
}
