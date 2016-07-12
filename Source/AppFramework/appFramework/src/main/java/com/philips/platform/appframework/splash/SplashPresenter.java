package com.philips.platform.appframework.splash;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class SplashPresenter implements UIBasePresenter {
    AppFrameworkApplication appFrameworkApplication;

    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        UICoCoUserRegImpl uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        if (uiCoCoUserReg.getUserObject(context).isUserSignIn()) {
            appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_HOME_STATE, context);
        } else if (AppFrameworkBaseActivity.getIntroScreenDonePressed() && !uiCoCoUserReg.getUserObject(context).isUserSignIn()) {
            appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_REGISTRATION_STATE, context);
        } else {
            appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_WELCOME_STATE, context);
        }

    }
}
