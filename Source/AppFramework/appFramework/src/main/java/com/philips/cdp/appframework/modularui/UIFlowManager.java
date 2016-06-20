package com.philips.cdp.appframework.modularui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIFlowManager {


    public static List<UIStateBase> stateBaseList;
    public @UIStateDefintions.UIStateDef
    static int startState;
    public @UIStateDefintions.UIStateDef
    static int currentState;
    public static Map<Integer, String> activityMap;

   public static void populateStateBaseList(){
        stateBaseList = new ArrayList<UIStateBase>();
        stateBaseList.add(new UIState(new UIWSNavigationStateOne(),UIStateDefintions.UI_SPLASH_STATE_ONE));

    }

    public static Map<Integer, String> getActivityMap() {
        return activityMap;
    }

    public static void setActivityMap(Map<Integer, String> activityMap) {
        UIFlowManager.activityMap = activityMap;
    }

    public static void populateActivityMap(){
        activityMap = new HashMap<Integer, String>();
        activityMap.put(UIStateDefintions.UI_SPLASH_STATE_ONE, "SplashActivity");
        activityMap.put(UIStateDefintions.UI_WELCOME_STATE_ONE, "HomeActivity");
        activityMap.put(UIStateDefintions.UI_WELCOME_STATE_TWO, "UserRegistration");
    }
}
