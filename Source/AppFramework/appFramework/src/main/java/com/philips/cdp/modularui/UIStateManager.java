package com.philips.cdp.modularui;

import android.content.Context;

import com.philips.cdp.appframework.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIStateManager {

    public static UIStateManager instance = new UIStateManager();

    private UIStateManager() {

    }

    public static UIStateManager getInstance() {
        if (null == instance) {
            instance = new UIStateManager();
        }
        return instance;
    }


    public Map<Integer,UIStateBase> uiStateMap;
    public List<UIStateBase> stateBaseList;
    public UIStateBase startState;

    public UIStateBase getCurrentState() {
        return currentState;
    }

    public void setCurrentState(UIStateBase currentState) {
        this.currentState = currentState;
    }

    public UIStateBase currentState;


    public void populateStateBaseList() {
        stateBaseList = new ArrayList<UIStateBase>();
        /*stateBaseList.add(new UIState(new UISplashUnRegisteredNavigator(), UIConstants.UI_SPLASH_UNREGISTERED_STATE));
        stateBaseList.add(new UIState(new UIWelcomeScreenNavigator(), UIConstants.UI_WELCOME_STATE));
        stateBaseList.add(new UIState(new UIUserRegNavigator(), UIConstants.UI_REGISTRATION_STATE));
        stateBaseList.add(new UIState(new UIHamburgerNavigator(), UIConstants.UI_HAMBURGER_STATE));
        stateBaseList.add(new UIState(new UIHamHomeNavigator(), UIConstants.UI_HAMBURGER_HOME_STATE_ONE));
        stateBaseList.add(new UIState(new UIHamSupportNavigator(), UIConstants.UI_HAMBURGER_SUPPORT_STATE_ONE));
        stateBaseList.add(new UIState(new UIHamSettingsNavigator(), UIConstants.UI_HAMBURGER_SETTINGS_STATE_ONE));
        stateBaseList.add(new UIState(new UIHamDebugNavigator(), UIConstants.UI_HAMBURGER_DEBUG_STATE_STATE_ONE));*/
    }

    public void populateStateList(){
        uiStateMap = new HashMap<Integer, UIStateBase>();
    }


    public void initAppStartState(Context mContext) {
        if(!SharedPreferenceUtility.getInstance().contains(UIConstants.UI_START_STATUS)){
            SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS,UIConstants.UI_SPLASH_UNREGISTERED_STATE);
        }
        currentState = getFromStateList(SharedPreferenceUtility.getInstance().getPreferenceInt(UIConstants.UI_START_STATUS));

    }

    public void addToStateList(UIStateBase uiStateBase) {
        if (null != stateBaseList) {
            stateBaseList.add(uiStateBase);
        }
    }

    public UIStateBase getFromStateList(@UIConstants.UIStateDef int stateID) {
        UIStateBase uiStateBaseItem = null;
        for (UIStateBase uiStateBase : stateBaseList) {
            if (uiStateBase.getStateID() == stateID) {
                uiStateBaseItem = uiStateBase;
                break;
            }
        }
        return uiStateBaseItem;
    }

}
