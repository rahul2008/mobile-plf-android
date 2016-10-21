/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanagerjson.jsonstates;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project           : Lumea
 * File Name         : States
 * Description       : Class to define the states
 * Revision History: version 1:
 * Date: 7/5/2016
 * Original author: Bhanu Hirawat
 * Description: Initial version
 */
public enum AppStates {
    SPLASH("splash"),
    WELCOME("welcome"),
    REGISTRATION("registration"),
    HOME("home"),
    HOMEFRAGMENT("homeFragment"),
    ABOUT("about"),
    DEBUG("debug"),
    IAP("iap"),
    PR("pr"),
    SUPPORT("support"),
    SETTINGS("settings"),;

    //Map to hold the Enum and its corresponding values.
    private static final Map<String, AppStates> ENUM_MAP;
    //Variable to hold stateValue
    private final String stateValue;

    /**
     * Parametrised Constructor to States Enum class
     *
     * @param stateValue value of the States
     */
    AppStates(String stateValue) {
        this.stateValue = stateValue;
    }

    /**
     * Returns the Enum value for given String value.
     *
     * @param value String value for which corresponding Enum value need to be returned.
     * @return Enum value for given String value.
     */
    public static AppStates get(String value) {
        return ENUM_MAP.get(value);
    }

    /**
     * This will return the value assigned to the state
     *
     * @return Value assigned to the state
     */
    public String getStateValue() {
        return this.stateValue;
    }

    /**
     * Build an immutable map of String name to enum pairs.
     */
    static {
        final Map<String, AppStates> map = new ConcurrentHashMap<String, AppStates>();
        for (final AppStates instance : AppStates.values()) {
            map.put(instance.getStateValue(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

}
