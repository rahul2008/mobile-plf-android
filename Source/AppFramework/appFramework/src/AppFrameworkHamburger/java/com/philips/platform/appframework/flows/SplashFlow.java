package com.philips.platform.appframework.flows;

import com.philips.platform.modularui.statecontroller.BaseFlow;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class SplashFlow implements BaseFlow {


    @Override
    public Integer getNextState(final String eventID) {
        switch (eventID) {
            case "splash_navigate_home":
                return UIState.UI_HOME_STATE;
            case "splash_navigate_welcome":
                return UIState.UI_WELCOME_STATE;
        }
        return null;
    }
}
