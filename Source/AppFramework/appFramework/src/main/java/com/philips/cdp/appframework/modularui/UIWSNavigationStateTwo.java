package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.R;

/**
 * Created by 310240027 on 6/17/2016.
 */
public class UIWSNavigationStateTwo implements UIBaseNavigation {
    @Override
    public
    @UIConstants.UIStateDef
    int onClick(int componentID, Context context) {
        @UIConstants.UIStateDef int destinationScreen = 0;
        UIState wsNavStateOne = (UIState) UIFlowManager.getFromStateList(UIConstants.UI_WELCOME_STATE_TWO);

        switch (componentID) {
            case R.id.start_registration_button:
                destinationScreen = wsNavStateOne.getStateID();
                break;
            case R.id.appframework_skip_button:
                destinationScreen = wsNavStateOne.getStateID();
                break;

        }

        return destinationScreen;
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
}
