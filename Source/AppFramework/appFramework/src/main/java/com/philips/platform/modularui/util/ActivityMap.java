package com.philips.platform.modularui.util;

import android.support.v4.app.Fragment;

import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.appframework.homescreen.HomeScreenFragment;
import com.philips.platform.appframework.settingscreen.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310240027 on 6/27/2016.
 */
public class ActivityMap {

    private static ActivityMap instance = new ActivityMap();

    private ActivityMap() {

    }

    public static ActivityMap getInstance() {
        if (null == instance) {
            instance = new ActivityMap();
        }
        return instance;
    }

    public Map<Integer, Integer> activityMap;

    public void populateActivityMap() {
        activityMap = new HashMap<Integer, Integer>();
        activityMap.put(UIConstants.UI_SPLASH_UNREGISTERED_STATE, UIConstants.UI_WELCOME_SCREEN);
        activityMap.put(UIConstants.UI_SPLASH_REGISTERED_STATE, UIConstants.UI_HAMBURGER_SCREEN);
        activityMap.put(UIConstants.UI_SPLASH_DONE_PRESSED_STATE, UIConstants.UI_USER_REGISTRATION_SCREEN);
        activityMap.put(UIConstants.UI_WELCOME_STATE, UIConstants.UI_WELCOME_SCREEN);
        activityMap.put(UIConstants.UI_HOME_STATE, UIConstants.UI_HAMBURGER_SCREEN);
        activityMap.put(UIConstants.UI_REGISTRATION_STATE, UIConstants.UI_USER_REGISTRATION_SCREEN);
        activityMap.put(UIConstants.UI_HOME_FRAGMENT_STATE, UIConstants.UI_HOME_SCREEN);
        activityMap.put(UIConstants.UI_SUPPORT_FRAGMENT_STATE, UIConstants.UI_SUPPORT_SCREEN);
        activityMap.put(UIConstants.UI_SETTINGS__FRAGMENT_STATE, UIConstants.UI_SETTINGS_SCREEN);
        activityMap.put(UIConstants.UI_DEBUG_FRAGMENT_STATE, UIConstants.UI_DEBUG_SCREEN);
    }

    @UIConstants.UIScreenConstants public int getFromActivityMap(int stateID){
        return activityMap.get(stateID);
    }

    public Map<Integer,Fragment> fragmentMap;

    public void populateFragmentMap(){
        fragmentMap = new HashMap<Integer,Fragment>();
        fragmentMap.put(UIConstants.UI_HOME_FRAGMENT_STATE, new HomeScreenFragment());
        fragmentMap.put(UIConstants.UI_SUPPORT_FRAGMENT_STATE, new HomeScreenFragment());
        fragmentMap.put(UIConstants.UI_SETTINGS__FRAGMENT_STATE,new SettingsFragment());
        fragmentMap.put(UIConstants.UI_DEBUG_FRAGMENT_STATE, new DebugTestFragment());

    }
    public Fragment getFragmentFromMap(int stateID){
        return fragmentMap.get(stateID);
    }
}
