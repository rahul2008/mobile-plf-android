package com.philips.cdp.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIUserRegNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {

        return null;
    }

    @Override
    public UIStateBase onPageLoad(Context context) {
        return  UIStateFactory.getInstance().createUIState(UIConstants.UI_HAMBURGER_STATE);
    }

    @Override
    public void setState() {
        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getFromStateList(UIConstants.UI_REGISTRATION_STATE));
    }
}
