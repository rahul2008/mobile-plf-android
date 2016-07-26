package com.philips.platform.appframework.introscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class WelcomePresenter extends UIBasePresenter implements UICoCoUserRegImpl.SetStateCallBack {

    WelcomePresenter(){
        setState(UIState.UI_WELCOME_STATE);
    }
    AppFrameworkApplication appFrameworkApplication;
    UICoCoUserRegImpl uiCoCoUserReg;

    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        switch (componentID) {

            case R.id.appframework_skip_button:
                SharedPreferenceUtility.getInstance().writePreferenceBoolean(UIConstants.DONE_PRESSED,true);
                uiCoCoUserReg.registerForNextState(this);
                appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_REGISTRATION_STATE, context, this);
                break;
            case R.id.start_registration_button:
                SharedPreferenceUtility.getInstance().writePreferenceBoolean(UIConstants.DONE_PRESSED,true);
                uiCoCoUserReg.registerForNextState(this);
                appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_REGISTRATION_STATE, context, this);
                break;
        }

    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_HOME_STATE, context, this);
    }


    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_HOME_STATE, context, this);
    }
}
