package com.philips.platform.appframework.splash;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.WelcomeRegistrationState;
import com.philips.platform.modularui.stateimpl.WelcomeState;
import com.philips.platform.modularui.util.UIConstants;

public class SplashPresenter extends UIBasePresenter implements UICoCoUserRegImpl.SetStateCallBack {
    SharedPreferenceUtility sharedPreferenceUtility;
    SplashPresenter(){
        setState(UIState.UI_SPLASH_STATE);
    }

    AppFrameworkApplication appFrameworkApplication;
    UICoCoUserRegImpl uiCoCoUserReg;
    UIState uiState;

    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        if (uiCoCoUserReg.getUserObject(context).isUserSignIn()) {
            uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        } else if (sharedPreferenceUtility.getPreferenceBoolean(UIConstants.DONE_PRESSED) && !uiCoCoUserReg.getUserObject(context).isUserSignIn()) {
            uiCoCoUserReg.registerForNextState(this);
            uiState = new WelcomeRegistrationState(UIState.UI_WELCOME_REGISTRATION_STATE);
        } else {
            uiState = new WelcomeState(UIState.UI_WELCOME_STATE);
        }
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);

    }

    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }
}
