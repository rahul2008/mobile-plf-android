package com.philips.platform.modularui.factorymanager;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.navigatorimpl.DebugTestFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeActivityNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.IntroductionScreenNavigator;
import com.philips.platform.modularui.navigatorimpl.SettingsFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.SupportFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.UserRegistrationNavigator;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateBase;

/**
 * Created by 310240027 on 7/7/2016.
 */
public class StateCreator {

    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;
    UIBaseNavigator uiBaseNavigator;

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
                    uiState = new UIState();
                    uiBaseNavigator = new IntroductionScreenNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_WELCOME_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new IntroductionScreenNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_REGISTRATION_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new UserRegistrationNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_HOME_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new HomeActivityNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_HOME_FRAGMENT_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new HomeFragmentNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_SUPPORT_FRAGMENT_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new SupportFragmentNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_SETTINGS_FRAGMENT_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new SettingsFragmentNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
                case UIStateBase.UI_DEBUG_FRAGMENT_STATE:
                    uiState = new UIState();
                    uiBaseNavigator = new DebugTestFragmentNavigator();
                    uiState.setStateID(stateID);
                    uiState.setNavigator(uiBaseNavigator);
                    break;
            }

            appFrameworkApplication.getFlowManager().addToStateMap(uiState);
        }

        return appFrameworkApplication.getFlowManager().getStateMap().get(stateID);
    }

}
