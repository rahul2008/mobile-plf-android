package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.R;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIWSNavigationStateOne implements UIBaseNavigation {
    @Override
    public  @UIFlowManager.UIStateDef int onClick(int componentID,Context context) {
        @UIFlowManager.UIStateDef int destinationScreen = 0;
        switch (componentID){
            case R.id.start_registration_button:
                destinationScreen = UIFlowManager.UI_WELCOME_STATE_ONE;

                break;
            case R.id.appframework_skip_button:
                destinationScreen = UIFlowManager.UI_WELCOME_STATE_ONE;

                break;

        }
        UIFlowManager.currentState = destinationScreen;
        return destinationScreen;
    }

    @Override
    public  @UIFlowManager.UIStateDef int onSwipe(int componentID,Context context) {
        return UIFlowManager.UI_SPLASH_STATE_ONE;
    }

    @Override
    public  @UIFlowManager.UIStateDef int onLongPress(int componentID,Context context) {
        return UIFlowManager.UI_SPLASH_STATE_ONE;
    }
}
