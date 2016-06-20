package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.R;

/**
 * Created by 310240027 on 6/17/2016.
 */
public class UIWSNavigationStateTwo implements UIBaseNavigation {
    @Override
    public @UIFlowManager.UIStateDef int onClick(int componentID, Context context) {
        @UIFlowManager.UIStateDef int destinationScreen = 0;
        switch (componentID){
            case R.id.start_registration_button:
                destinationScreen = UIFlowManager.UI_WELCOME_STATE_TWO;
                UIFlowManager.currentState = UIFlowManager.UI_WELCOME_STATE_TWO;
                break;
            case R.id.appframework_skip_button:
                destinationScreen = UIFlowManager.UI_WELCOME_STATE_TWO;
                UIFlowManager.currentState = UIFlowManager.UI_WELCOME_STATE_TWO;
                break;

        }
        return destinationScreen;
    }

    @Override
    public @UIFlowManager.UIStateDef int onSwipe(int componentID, Context context) {
        return UIFlowManager.UI_SPLASH_STATE_ONE;
    }

    @Override
    public @UIFlowManager.UIStateDef int onLongPress(int componentID, Context context) {
        return UIFlowManager.UI_SPLASH_STATE_ONE;
    }
}
