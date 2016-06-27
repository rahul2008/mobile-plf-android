package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.utility.SharedPreferenceUtility;

/**
 * Created by 310240027 on 6/17/2016.
 */
public class UIWSNavigationStateTwo implements UIBaseNavigation {
    @Override
    public
    @UIConstants.UIStateDef
    int onClick(int componentID, Context context) {
        return 0;
    }

    @Override
    public
    @UIConstants.UIStateDef
    int onSwipe(int componentID, Context context) {
        return UIConstants.UI_SPLASH_STATE_ONE;
    }

    @Override
    public
    @UIConstants.UIStateDef
    int onLongPress(int componentID, Context context) {
        return UIConstants.UI_SPLASH_STATE_ONE;
    }

    @Override
    public int onPageLoad(Context context) {

        @UIConstants.UIStateDef int destinationScreen = 0;
        SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS, UIConstants.UI_SPLASH_STATE_TWO);
        destinationScreen = UIConstants.UI_HAMBURGER_STATE;

        return destinationScreen;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_WELCOME_STATE_TWO);
    }
}
