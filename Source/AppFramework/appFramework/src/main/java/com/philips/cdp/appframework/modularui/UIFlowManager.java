package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIFlowManager {


    public static List<UIStateBase> stateBaseList;
    public static UIStateBase startState;
    public static UIStateBase currentState;
    public static Map<Integer, Integer> activityMap;

    public static void populateStateBaseList() {
        stateBaseList = new ArrayList<UIStateBase>();
        stateBaseList.add(new UIState(new UISplashNavigationState(), UIConstants.UI_SPLASH_STATE));
        stateBaseList.add(new UIState(new UIWelcomeScreenState(), UIConstants.UI_WELCOME_STATE));
        stateBaseList.add(new UIState(new UIUserRegNavigationState(), UIConstants.UI_REGISTRATION_STATE));
        stateBaseList.add(new UIState(new UIHamburgerNavigationState(), UIConstants.UI_HAMBURGER_STATE));
        stateBaseList.add(new UIState(new UIHamHomeNavigationStateOne(), UIConstants.UI_HAMBURGER_HOME_STATE_ONE));
        stateBaseList.add(new UIState(new UIHamSupportNavigationStateOne(), UIConstants.UI_HAMBURGER_SUPPORT_STATE_ONE));
        stateBaseList.add(new UIState(new UIHamSettingsNavigationStateOne(), UIConstants.UI_HAMBURGER_SETTINGS_STATE_ONE));
        stateBaseList.add(new UIState(new UIHamDebugNavigationStateOne(), UIConstants.UI_HAMBURGER_DEBUG_STATE_STATE_ONE));
    }

    public static void checkUserSignInAndDonePressed(Context mContext) {
        if(!SharedPreferenceUtility.getInstance().contains(UIConstants.UI_START_STATUS)){
            SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS,UIConstants.UI_SPLASH_STATE);
        }
        currentState = getFromStateList(SharedPreferenceUtility.getInstance().getPreferenceInt(UIConstants.UI_START_STATUS));

    }

    public static void addToStateList(UIStateBase uiStateBase) {
        if (null != stateBaseList) {
            stateBaseList.add(uiStateBase);
        }
    }

    public static UIStateBase getFromStateList(@UIConstants.UIStateDef int stateID) {
        UIStateBase uiStateBaseItem = null;
        for (UIStateBase uiStateBase : stateBaseList) {
            if (uiStateBase.getStateID() == stateID) {
                uiStateBaseItem = uiStateBase;
                break;
            }
        }
        return uiStateBaseItem;
    }

    public static Map<Integer, Integer> getActivityMap() {
        return activityMap;
    }

    public static void setActivityMap(Map<Integer, Integer> activityMap) {
        UIFlowManager.activityMap = activityMap;
    }

    public static void populateActivityMap() {
        activityMap = new HashMap<Integer, Integer>();
        activityMap.put(UIConstants.UI_SPLASH_STATE, UIConstants.UI_WELCOME_SCREEN);
        activityMap.put(UIConstants.UI_WELCOME_STATE, UIConstants.UI_WELCOME_SCREEN);
        activityMap.put(UIConstants.UI_HAMBURGER_STATE, UIConstants.UI_HAMBURGER_SCREEN);
        activityMap.put(UIConstants.UI_REGISTRATION_STATE, UIConstants.UI_USER_REGISTRATION_SCREEN);
        activityMap.put(UIConstants.UI_HAMBURGER_HOME_STATE_ONE, UIConstants.UI_HOME_SCREEN);
        activityMap.put(UIConstants.UI_HAMBURGER_SUPPORT_STATE_ONE, UIConstants.UI_SUPPORT_SCREEN);
        activityMap.put(UIConstants.UI_HAMBURGER_SETTINGS_STATE_ONE, UIConstants.UI_SETTINGS_SCREEN);
        activityMap.put(UIConstants.UI_HAMBURGER_DEBUG_STATE_STATE_ONE, UIConstants.UI_DEBUG_SCREEN);
    }
}
