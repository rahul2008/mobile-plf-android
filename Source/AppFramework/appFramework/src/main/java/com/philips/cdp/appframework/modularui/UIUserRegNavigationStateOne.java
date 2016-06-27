package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIUserRegNavigationStateOne implements UIBaseNavigation {
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
        return UIConstants.UI_HAMBURGER_STATE_ONE;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_REGISTRATION_STATE_ONE);
    }
}
