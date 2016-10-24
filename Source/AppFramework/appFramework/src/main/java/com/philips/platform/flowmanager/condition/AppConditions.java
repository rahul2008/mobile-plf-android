/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanager.condition;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project           : Lumea
 * File Name         : Conditions
 * Description       : Class to define the conditions
 * Revision History: version 1:
 * Date: 7/12/2016
 * Original author: Bhanu Hirawat
 * Description: Initial version
 */
public enum AppConditions {
    IS_LOGGED_IN("isLoggedIn"),
    IS_DONE_PRESSED("isDonePressed");

    //Map to hold the Enum and its corresponding values.
    private static final Map<String, AppConditions> ENUM_MAP;
    //Variable to hold conditionValue
    private final String conditionValue;

    /**
     * Parametrised Constructor to Conditions Enum class
     *
     * @param conditionValue value of the Conditions
     */
    AppConditions(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    /**
     * Returns the Enum value for given String value.
     *
     * @param value String value for which corresponding Enum value need to be returned.
     * @return Enum value for given String value.
     */
    public static AppConditions get(String value) {
        return ENUM_MAP.get(value);
    }

    /**
     * This will return the value assigned to the condition
     *
     * @return Value assigned to the Condition
     */
    public String getConditionValue() {
        return this.conditionValue;
    }

    /**
     * Build an immutable map of String name to enum pairs.
     */
    static {
        final Map<String, AppConditions> map = new ConcurrentHashMap<String, AppConditions>();
        for (final AppConditions instance : AppConditions.values()) {
            map.put(instance.getConditionValue(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

}
