package com.philips.cdp.modularui;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310240027 on 6/27/2016.
 */
public class ActivityMap {

    public static Map<Integer, Integer> activityMap;
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
