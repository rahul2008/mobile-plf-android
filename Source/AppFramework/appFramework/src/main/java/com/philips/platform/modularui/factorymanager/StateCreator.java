package com.philips.platform.modularui.factorymanager;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.navigatorimpl.DebugTestFragmentState;
import com.philips.platform.modularui.navigatorimpl.HomeActivityState;
import com.philips.platform.modularui.navigatorimpl.HomeFragmentState;
import com.philips.platform.modularui.navigatorimpl.IntroductionScreenState;
import com.philips.platform.modularui.navigatorimpl.SettingsFragmentState;
import com.philips.platform.modularui.navigatorimpl.SupportFragmentState;
import com.philips.platform.modularui.navigatorimpl.UserRegistrationState;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateBase;

/**
 * Created by 310240027 on 7/7/2016.
 */
public class StateCreator {

    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;
    UIState uiBaseNavigator;

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

                case UIStateBase.UI_SPLASH_UNREGISTERED_STATE:
                    uiBaseNavigator = new IntroductionScreenState(UIStateBase.UI_SPLASH_UNREGISTERED_STATE);
                    break;
                case UIStateBase.UI_WELCOME_STATE:
                    uiBaseNavigator = new IntroductionScreenState(UIStateBase.UI_WELCOME_STATE);
                    break;
                case UIStateBase.UI_REGISTRATION_STATE:
                    uiBaseNavigator = new UserRegistrationState(UIStateBase.UI_REGISTRATION_STATE);
                    break;
                case UIStateBase.UI_HOME_STATE:
                    uiBaseNavigator = new HomeActivityState(UIStateBase.UI_HOME_STATE);
                    break;
                case UIStateBase.UI_HOME_FRAGMENT_STATE:
                    uiBaseNavigator = new HomeFragmentState(UIStateBase.UI_HOME_FRAGMENT_STATE);
                    break;
                case UIStateBase.UI_SUPPORT_FRAGMENT_STATE:
                    uiBaseNavigator = new SupportFragmentState(UIStateBase.UI_SUPPORT_FRAGMENT_STATE);
                    break;
                case UIStateBase.UI_SETTINGS_FRAGMENT_STATE:
                    uiBaseNavigator = new SettingsFragmentState(UIStateBase.UI_SETTINGS_FRAGMENT_STATE);
                    break;
                case UIStateBase.UI_DEBUG_FRAGMENT_STATE:
                    uiBaseNavigator = new DebugTestFragmentState(UIStateBase.UI_DEBUG_FRAGMENT_STATE);
                    break;
            }

            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }

        return appFrameworkApplication.getFlowManager().getStateMap().get(stateID);
    }

}
