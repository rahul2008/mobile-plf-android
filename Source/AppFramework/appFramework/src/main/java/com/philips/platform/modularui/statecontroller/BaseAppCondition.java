package com.philips.platform.modularui.statecontroller;

import com.philips.platform.flowmanager.condition.BaseCondition;

import java.util.Map;
import java.util.TreeMap;

public class BaseAppCondition {

    protected Map<String, BaseCondition> baseConditionMap;

    public BaseAppCondition() {
        baseConditionMap = new TreeMap<>();
    }

    public BaseCondition getCondition(String key) {
        return baseConditionMap.get(key);
    }
}
