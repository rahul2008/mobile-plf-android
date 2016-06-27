package com.philips.cdp.appframework.modularui;

import android.content.Context;

/**
 * Created by 310240027 on 6/27/2016.
 */
public class UIWSNavigationState implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        return UIConstants.UI_WELCOME_STATE_TWO;
    }

    @Override
    public int onSwipe(int componentID, Context context) {
        return 0;
    }

    @Override
    public int onLongPress(int componentID, Context context) {
        return 0;
    }

    @Override
    public int onPageLoad(Context context) {
        return 0;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_WELCOME_STATE);
    }
}
