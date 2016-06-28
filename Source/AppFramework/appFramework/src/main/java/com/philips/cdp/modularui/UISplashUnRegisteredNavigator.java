package com.philips.cdp.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/21/2016.
 */
public class UISplashUnRegisteredNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {
        return null;
    }

    @Override
    public UIStateBase onPageLoad(Context context) {
        return UIStateFactory.getInstance().createUIState(UIConstants.UI_WELCOME_STATE);
    }

    @Override
    public void setState() {
        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getStateMap(UIConstants.UI_SPLASH_UNREGISTERED_STATE));
    }
}
