package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.introscreen.IntroductionScreenActivity;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIUserRegNavigationStateOne implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        @UIStateDefintions.UIStateDef int destinationScreen = 0;
        UIState wsNavStateOne = new UIState(this,destinationScreen);
        wsNavStateOne.setStateID(UIStateDefintions.UI_REGISTRATION_STATE_ONE);
        wsNavStateOne.setNavigator(this);

        switch (componentID){
            case IntroductionScreenActivity.userRegistrationClickID:
                destinationScreen = wsNavStateOne.getStateID() ;
                break;

        }

        return destinationScreen;
    }

    @Override
    public int onSwipe(int componentID, Context context) {
        return UIStateDefintions.UI_SPLASH_STATE_ONE;
    }

    @Override
    public int onLongPress(int componentID, Context context) {
        return UIStateDefintions.UI_SPLASH_STATE_ONE;
    }
}
