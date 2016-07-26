package com.philips.platform.modularui.factorymanager;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeActivityState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IntroductionScreenState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;

/**
 * Created by 310240027 on 7/7/2016.
 */
public class StateCreator {

    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;

    private StateCreator(){
    }

    private static StateCreator _instance = new StateCreator();

    public static StateCreator getInstance(){
        if(null == _instance){
            _instance = new StateCreator();
        }
        return  _instance;
    }

    public UIState getState(int stateID, Context context){
        appFrameworkApplication = (AppFrameworkApplication)context.getApplicationContext();
        if(!appFrameworkApplication.getFlowManager().getStateMap().containsKey(stateID)){
            switch (stateID){

                case UIState.UI_SPLASH_UNREGISTERED_STATE:
                    uiState = new IntroductionScreenState(UIState.UI_SPLASH_UNREGISTERED_STATE);
                    break;
                case UIState.UI_WELCOME_STATE:
                    uiState = new IntroductionScreenState(UIState.UI_WELCOME_STATE);
                    break;
                case UIState.UI_REGISTRATION_STATE:
                    uiState = new UserRegistrationState(UIState.UI_REGISTRATION_STATE);
                    break;
                case UIState.UI_HOME_STATE:
                    uiState = new HomeActivityState(UIState.UI_HOME_STATE);
                    break;
                case UIState.UI_HOME_FRAGMENT_STATE:
                    uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                    break;
                case UIState.UI_SUPPORT_FRAGMENT_STATE:
                    uiState = new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE);
                    break;
                case UIState.UI_SETTINGS_FRAGMENT_STATE:
                    uiState = new SettingsFragmentState(UIState.UI_SETTINGS_FRAGMENT_STATE);
                    break;
                case UIState.UI_DEBUG_FRAGMENT_STATE:
                    uiState = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
                    break;
            }

            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }

        return appFrameworkApplication.getFlowManager().getStateMap().get(stateID);
    }

}
