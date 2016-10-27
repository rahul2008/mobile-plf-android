package com.philips.platform.modularui.statecontroller;

import com.philips.platform.flowmanager.condition.BaseCondition;

import java.util.Map;
import java.util.TreeMap;

public class BaseAppCondition {

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String IS_DONE_PRESSED = "isDonePressed";
    public static final String CONDITION_APP_LAUNCH = "conditionAppLaunch";
    protected Map<String, BaseCondition> baseConditionMap;

    public BaseAppCondition() {
        baseConditionMap = new TreeMap<>();
    }

    public BaseCondition getCondition(String key) {
        return baseConditionMap.get(key);
    }
}
