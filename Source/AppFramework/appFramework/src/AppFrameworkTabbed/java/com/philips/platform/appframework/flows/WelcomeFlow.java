package com.philips.platform.appframework.flows;

import com.philips.platform.modularui.statecontroller.BaseFlow;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class WelcomeFlow implements BaseFlow {

    @Override
    public Integer getNextState(final String eventID) {
        switch (eventID) {
            case "welcome_done":
                return UIState.UI_USER_REGISTRATION_STATE;
            case "welcome_skip":
                return UIState.UI_USER_REGISTRATION_STATE;
            case "welcome_ur":
                return UIState.UI_USER_REGISTRATION_STATE;
            case "welcome_fragment_home":
                return UIState.UI_HOME_FRAGMENT_STATE;
            case "welcome_activity_home":
                return UIState.UI_HOME_STATE;
            case "welcome_settings":
                return UIState.UI_SETTINGS_FRAGMENT_STATE;
        }
        return null;
    }
}
