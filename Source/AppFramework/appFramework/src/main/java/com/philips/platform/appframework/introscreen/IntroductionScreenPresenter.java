package com.philips.platform.appframework.introscreen;

import android.content.Context;

import com.philips.platform.appframework.R;
import com.philips.platform.modularui.navigatorimpl.UserRegistrationNavigator;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class IntroductionScreenPresenter implements UIBasePresenter {
    UIBaseNavigator uiBaseNavigator;
    @Override
    public void onClick(int componentID, Context context) {
        switch (componentID){
            case R.id.appframework_skip_button:
                uiBaseNavigator = new UserRegistrationNavigator();
                break;
            case R.id.start_registration_button:
                uiBaseNavigator = new UserRegistrationNavigator();
                break;
        }

        uiBaseNavigator.loadScreen(context);
    }
}
