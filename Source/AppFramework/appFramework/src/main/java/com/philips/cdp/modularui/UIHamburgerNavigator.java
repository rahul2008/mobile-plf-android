package com.philips.cdp.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/24/2016.
 */
public class UIHamburgerNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {
        UIStateBase destinationScreen = null;
        switch (componentID){
            case 0: destinationScreen =  UIStateFactory.getInstance().createUIState(UIConstants.UI_HAMBURGER_HOME_STATE_ONE);
                break;
            case 1: destinationScreen =  UIStateFactory.getInstance().createUIState(UIConstants.UI_HAMBURGER_SUPPORT_STATE_ONE);
                break;
            case 2: destinationScreen =  UIStateFactory.getInstance().createUIState(UIConstants.UI_HAMBURGER_SETTINGS_STATE_ONE);
                break;
            case 3: destinationScreen =  UIStateFactory.getInstance().createUIState(UIConstants.UI_HAMBURGER_DEBUG_STATE_STATE_ONE);
                break;
        }
        return destinationScreen;
    }


    @Override
    public UIStateBase onPageLoad(Context context) {
        return null;
    }

    @Override
    public void setState() {
        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getStateMap(UIConstants.UI_HAMBURGER_STATE));
    }
}
