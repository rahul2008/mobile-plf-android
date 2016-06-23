package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.introscreen.IntroductionScreenActivity;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIUserRegNavigationStateOne implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        @UIConstants.UIStateDef int destinationScreen = 0;
        UIState wsNavStateOne = new UIState(this, destinationScreen);
        wsNavStateOne.setStateID(UIConstants.UI_HOME_SCREEN);
        wsNavStateOne.setNavigator(this);

        switch (componentID) {
            case IntroductionScreenActivity.userRegistrationClickID:
                destinationScreen = wsNavStateOne.getStateID();
                break;

        }

        return destinationScreen;
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
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_WELCOME_STATE_ONE);
    }
}
