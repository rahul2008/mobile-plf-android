package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/21/2016.
 */
public class UISplashNavigationStateTwo implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        @UIConstants.UIStateDef int destinationScreen = 0;
        UIState wsNavStateOne = (UIState) UIFlowManager.getFromStateList(UIConstants.UI_REGISTRATION_STATE_ONE);
        return wsNavStateOne.getStateID();
    }

    @Override
    public int onSwipe(int componentID, Context context) {
        return UIConstants.UI_SPLASH_STATE_ONE;
    }

    @Override
    public int onLongPress(int componentID, Context context) {
        return UIConstants.UI_SPLASH_STATE_ONE;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_SPLASH_STATE_TWO);
    }
}
