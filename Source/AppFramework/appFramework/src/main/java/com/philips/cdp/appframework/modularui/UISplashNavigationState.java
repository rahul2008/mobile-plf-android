package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/21/2016.
 */
public class UISplashNavigationState implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        return UIConstants.UI_WELCOME_STATE;
    }

    @Override
    public int onPageLoad(Context context) {
        return UIConstants.UI_WELCOME_STATE;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_SPLASH_STATE);
    }
}
