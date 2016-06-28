package com.philips.cdp.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/28/2016.
 */
public class UISplashDonePressedNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {
        return null;
    }

    @Override
    public UIStateBase onPageLoad(Context context) {
        return UIStateFactory.getInstance().createUIState(UIConstants.UI_REGISTRATION_STATE);
    }

    @Override
    public void setState() {

    }
}
