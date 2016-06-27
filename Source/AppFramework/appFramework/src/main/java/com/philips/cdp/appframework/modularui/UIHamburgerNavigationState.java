package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/24/2016.
 */
public class UIHamburgerNavigationState implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        @UIConstants.UIStateDef int destinationScreen = 0;
        switch (componentID){
            case 0: destinationScreen = UIConstants.UI_HAMBURGER_HOME_STATE_ONE;
                break;
            case 1: destinationScreen = UIConstants.UI_HAMBURGER_SUPPORT_STATE_ONE;
                break;
            case 2: destinationScreen = UIConstants.UI_HAMBURGER_SETTINGS_STATE_ONE;
                break;
            case 3: destinationScreen = UIConstants.UI_HAMBURGER_DEBUG_STATE_STATE_ONE;
                break;
        }
        return destinationScreen;
    }

    @Override
    public int onSwipe(int componentID, Context context) {
        return UIConstants.UI_HAMBURGER_HOME_STATE_ONE;
    }

    @Override
    public int onLongPress(int componentID, Context context) {
        return UIConstants.UI_HAMBURGER_HOME_STATE_ONE;
    }

    @Override
    public int onPageLoad(Context context) {
        return 0;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_HAMBURGER_STATE);
    }
}
