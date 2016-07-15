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
public class SplashPresenter implements UIBasePresenter,UICoCoUserRegImpl.SetStateCallBack {
    AppFrameworkApplication appFrameworkApplication;
    UICoCoUserRegImpl uiCoCoUserReg;

    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {
            //TODO:Better way to access Application class
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        if (uiCoCoUserReg.getUserObject(context).isUserSignIn()) {
            //TODO : Move state constants to StateClass, rename navigateNextState method in flowmanager
            appFrameworkApplication.getFlowManager().navigateState(UIConstants.UI_HOME_STATE, context);
            //TODO : move this logic out of base activity
        } else if (AppFrameworkBaseActivity.getIntroScreenDonePressed() && !uiCoCoUserReg.getUserObject(context).isUserSignIn()) {
            //TODO: refer UGrow for accessing flowmanager through application class
            uiCoCoUserReg.registerForNextState(this);
            appFrameworkApplication.getFlowManager().navigateState(UIConstants.UI_REGISTRATION_STATE, context);
        } else {
            appFrameworkApplication.getFlowManager().navigateState(UIConstants.UI_WELCOME_STATE, context);
        }

    }

    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateState(UIConstants.UI_HOME_STATE, context);
    }
}
