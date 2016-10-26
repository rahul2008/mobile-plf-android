package com.philips.platform.modularui.statecontroller;

import java.util.Map;
import java.util.TreeMap;

public class BaseAppState {

    public static final String WELCOME = "welcome";
    public static final String HOME = "home";
    public static final String SUPPORT = "support";
    public static final String SPLASH = "splash";
    public static final String REGISTRATION = "registration";
    public static final String HOME_FRAGMENT = "home_fragment";
    public static final String ABOUT = "about";
    public static final String DEBUG = "debug";
    public static final String IAP = "iap";
    public static final String PR = "pr";
    public static final String SETTINGS = "settings";

    protected Map<String, BaseState> stateMap;

    public BaseAppState() {
        stateMap = new TreeMap<>();
    }

    public BaseState getState(String key) {
        return stateMap.get(key);
    }
}
