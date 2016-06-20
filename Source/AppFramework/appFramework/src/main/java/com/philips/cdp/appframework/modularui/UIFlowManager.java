package com.philips.cdp.appframework.modularui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIFlowManager {
    @IntDef({UI_SPLASH_STATE_ONE, UI_WELCOME_STATE_ONE,UI_WELCOME_STATE_TWO,UI_WELCOME_STATE_THREE, UI_REGISTRATION_STATE_ONE,UI_HOME_STATE_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {}

    public static final int UI_SPLASH_STATE_ONE = 1001;
    public static final int UI_WELCOME_STATE_ONE = 1002;
    public static final int UI_WELCOME_STATE_TWO = 1003;
    public static final int UI_WELCOME_STATE_THREE = 1004;
    public static final int UI_HOME_STATE_ONE = 1005;
    public static final int UI_REGISTRATION_STATE_ONE = 1006;

    public static List<UIStateBase> stateBaseList;
    public @UIStateDef static int startState;
    public @UIStateDef static int currentState;
    public static Map<Integer, String> activityMap;

   public static void populateStateBaseList(){
        stateBaseList = new ArrayList<UIStateBase>();
        stateBaseList.add(new UIState(new UIWSNavigationStateOne(),UI_SPLASH_STATE_ONE));

    }

    public static Map<Integer, String> getActivityMap() {
        return activityMap;
    }

    public static void setActivityMap(Map<Integer, String> activityMap) {
        UIFlowManager.activityMap = activityMap;
    }

    public static void populateActivityMap(){
        activityMap = new HashMap<Integer, String>();
        activityMap.put(UI_SPLASH_STATE_ONE, "SplashActivity");
        activityMap.put(UI_WELCOME_STATE_ONE, "HomeActivity");
        activityMap.put(UI_WELCOME_STATE_TWO, "UserRegistration");
    }
}
