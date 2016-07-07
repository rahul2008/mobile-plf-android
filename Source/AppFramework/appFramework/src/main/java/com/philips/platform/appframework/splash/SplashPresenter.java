package com.philips.platform.appframework.splash;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.statecontroller.ShowFragmentCallBack;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class SplashPresenter implements UIBasePresenter {
    User userRegistration;
    AppFrameworkApplication appFrameworkApplication;

    @Override
    public void onClick(int componentID, Context context, ShowFragmentCallBack showFragmentCallBack) {

    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        userRegistration = new User(context.getApplicationContext());
        if (userRegistration.isUserSignIn()) {
            appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_HOME_STATE, context);
        } else if (AppFrameworkBaseActivity.getIntroScreenDonePressed() && !userRegistration.isUserSignIn()) {
            appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_REGISTRATION_STATE, context);
        } else {
            appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_WELCOME_STATE, context);
        }

    }
}
