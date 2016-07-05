package com.philips.platform.appframework.splash;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.navigatorimpl.UserRegistrationNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeActivityNavigator;
import com.philips.platform.modularui.navigatorimpl.IntroductionScreenNavigator;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class SplashPresenter implements UIBasePresenter {
    User userRegistration;
    UIBaseNavigator uiBaseNavigator;
    @Override
    public void onClick(int componentID, Context context) {
        userRegistration = new User(context.getApplicationContext());
        if(userRegistration.isUserSignIn()){
            uiBaseNavigator = new HomeActivityNavigator();
        }
        else if(AppFrameworkBaseActivity.getIntroScreenDonePressed() && !userRegistration.isUserSignIn()){
            uiBaseNavigator = new UserRegistrationNavigator();
        }else {
            uiBaseNavigator = new IntroductionScreenNavigator();
        }

        uiBaseNavigator.loadScreen(context);

    }
}
