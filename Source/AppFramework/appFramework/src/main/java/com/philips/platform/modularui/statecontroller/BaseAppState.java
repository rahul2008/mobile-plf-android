package com.philips.platform.modularui.statecontroller;

import java.util.Map;
import java.util.TreeMap;

public class BaseAppState {

    public static final String WELCOME = "welcome";
    protected Map<String, BaseState> stateMap;

    public BaseAppState() {
        stateMap = new TreeMap<>();
    }

    public BaseState getState(String key) {
        return stateMap.get(key);
    }
}
