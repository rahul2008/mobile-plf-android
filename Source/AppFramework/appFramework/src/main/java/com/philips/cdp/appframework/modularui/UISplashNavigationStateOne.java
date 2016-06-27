package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/21/2016.
 */
public class UISplashNavigationStateOne implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        return UIConstants.UI_HAMBURGER_STATE_ONE;
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
    public int onPageLoad(Context context) {
        return 0;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_SPLASH_STATE_ONE);
    }
}
