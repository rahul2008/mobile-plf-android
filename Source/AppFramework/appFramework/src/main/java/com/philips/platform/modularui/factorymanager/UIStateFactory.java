package com.philips.platform.modularui.factorymanager;

import com.philips.platform.modularui.navigatorimpl.DebugTestFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeActivityNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.IntroductionScreenNavigator;
import com.philips.platform.modularui.navigatorimpl.SettingsFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.SupportFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.UserRegistrationNavigator;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateBase;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 6/28/2016.
 */
public class UIStateFactory {

    private static UIStateFactory instance = new UIStateFactory();

    private UIStateFactory(){

    }

    public static UIStateFactory getInstance(){
        if(null == instance){
            instance = new UIStateFactory();
        }
        return instance;
    }


    public UIStateBase createUIState(@UIConstants.UIStateDef int uiStateID){

        UIState uistate = new UIState();
        if(!UIFlowManager.getInstance().getUiStateMap().containsKey(uiStateID)){
            switch (uiStateID) {
                case UIConstants.UI_WELCOME_STATE:
                    uistate.setStateID(UIConstants.UI_WELCOME_STATE);
                    uistate.setNavigator(new IntroductionScreenNavigator());
                    break;
                case UIConstants.UI_REGISTRATION_STATE:
                    uistate.setStateID(UIConstants.UI_REGISTRATION_STATE);
                    uistate.setNavigator(new UserRegistrationNavigator());
                    break;
                case UIConstants.UI_HOME_STATE:
                    uistate.setStateID(UIConstants.UI_HOME_STATE);
                    uistate.setNavigator(new HomeActivityNavigator());
                    break;
                case UIConstants.UI_HOME_FRAGMENT_STATE:
                    uistate.setStateID(UIConstants.UI_HOME_FRAGMENT_STATE);
                    uistate.setNavigator(new HomeFragmentNavigator());
                    break;
                case UIConstants.UI_SUPPORT_FRAGMENT_STATE:
                    uistate.setStateID(UIConstants.UI_SUPPORT_FRAGMENT_STATE);
                    uistate.setNavigator(new SupportFragmentNavigator());
                    break;
                case UIConstants.UI_SETTINGS__FRAGMENT_STATE:
                    uistate.setStateID(UIConstants.UI_SETTINGS__FRAGMENT_STATE);
                    uistate.setNavigator(new SettingsFragmentNavigator());
                    break;
                case UIConstants.UI_DEBUG_FRAGMENT_STATE:
                    uistate.setStateID(UIConstants.UI_DEBUG_FRAGMENT_STATE);
                    uistate.setNavigator(new DebugTestFragmentNavigator());
                    break;
            }
            UIFlowManager.getInstance().addToStateList(uistate);
            return uistate;
        }else {
           return UIFlowManager.getInstance().getStateMap(uiStateID);
        }

    }
}
