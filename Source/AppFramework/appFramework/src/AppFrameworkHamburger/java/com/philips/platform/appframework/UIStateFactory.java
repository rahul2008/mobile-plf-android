/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework;

import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SplashState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.stateimpl.WelcomeState;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UIStateFactory {

    //Map to hold the Enum and its corresponding values.
    private static final Map<AppStates, UIState> UI_STATE_MAP;

    /**
     * This method will creates and return the object of type of BaseUIState depending upon stateID
     *
     * @param state StateID for which the object need to be created
     * @return Objects 'BaseUIState' based on StateID
     */
    public UIState getUIStateState(AppStates state) {
        return UI_STATE_MAP.get(state);
    }

    /**
     * Build an immutable map of UI states type and corresponding class object pairs.
     */
    static {
        final Map<AppStates, UIState> map = new ConcurrentHashMap<AppStates, UIState>();
        //map.put(AppStates.SPLASH,new UIStateSplash());
        map.put(AppStates.WELCOME, new WelcomeState());
        map.put(AppStates.REGISTRATION, new UserRegistrationState());
        map.put(AppStates.HOMEFRAGMENT, new HomeFragmentState());
        map.put(AppStates.HOME, new HomeActivityState());
        map.put(AppStates.ABOUT, new AboutScreenState());
        map.put(AppStates.DEBUG, new DebugTestFragmentState());
        map.put(AppStates.SETTINGS, new SettingsFragmentState());
        map.put(AppStates.IAP, new IAPState());
        map.put(AppStates.PR, new ProductRegistrationState());
        map.put(AppStates.SUPPORT, new SupportFragmentState());
        map.put(AppStates.SPLASH, new SplashState());

        UI_STATE_MAP = Collections.unmodifiableMap(map);
    }
}
