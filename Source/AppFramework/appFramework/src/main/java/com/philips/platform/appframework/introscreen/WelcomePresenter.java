package com.philips.platform.appframework.introscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class WelcomePresenter extends UIBasePresenter implements UICoCoUserRegImpl.SetStateCallBack {

    public WelcomePresenter(){

    }
    AppFrameworkApplication appFrameworkApplication;
    UICoCoUserRegImpl uiCoCoUserReg;
    SharedPreferenceUtility sharedPreferenceUtility;
    UIState uiState;

    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        uiCoCoUserReg.setFragmentContainer(R.id.fragment_frame_container);
        ((WelcomeActivity)context).changeActionBarState(true);
        switch (componentID) {

            case R.id.appframework_skip_button:
                uiCoCoUserReg.registerForNextState(this);
                uiCoCoUserReg.setFragActivity((WelcomeActivity)context);
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                break;
            case R.id.start_registration_button:
                sharedPreferenceUtility = new SharedPreferenceUtility(context);
                sharedPreferenceUtility.writePreferenceBoolean(UIConstants.DONE_PRESSED,true);
                uiCoCoUserReg.registerForNextState(this);
                uiCoCoUserReg.setFragActivity((WelcomeActivity)context);
                uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
                break;
        }
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);

    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
        if(appFrameworkApplication.getFlowManager().getCurrentState().getStateID() == UIState.UI_WELCOME_REGISTRATION_STATE){
            ((WelcomeActivity)context).changeActionBarState(true);
            setState(UIState.UI_WELCOME_REGISTRATION_STATE);
            uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
            uiCoCoUserReg.registerForNextState(this);
            uiCoCoUserReg.setFragActivity((WelcomeActivity)context);
            uiCoCoUserReg.setFragmentContainer(R.id.fragment_frame_container);
            uiState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
            uiState.setPresenter(this);
            appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
        }
        else {
            setState(UIState.UI_WELCOME_STATE);
            appFrameworkApplication.getFlowManager().getCurrentState().setStateID(UIState.UI_WELCOME_STATE);
            ((WelcomeActivity)context).changeActionBarState(false);
            ((WelcomeActivity)context).loadWelcomeFragment();
        }

    }


    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new HomeActivityState(UIState.UI_HOME_STATE);
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }
}
