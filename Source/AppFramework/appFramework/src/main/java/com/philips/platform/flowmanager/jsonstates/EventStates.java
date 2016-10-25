package com.philips.platform.flowmanager.jsonstates;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */
public enum EventStates {

    APP_START("onAppStartEvent"),
    WELCOME_SKIP("welcome_skip"),
    WELCOME_DONE("welcome_done"),
    WELCOME_HOME("welcome_home"),
    REGISTRATION("welcome_registration"),
    HOME_FRAGMENT("home_fragment"),
    HOME_SUPPORT("support"),
    HOME_IAP("iap"),
    HOME_ABOUT("about"),
    HOME_PR("pr"),
    HOME_SHOPPING_CART("shopping_cart"),
    HOME_SETTINGS("settings"),;

    //Map to hold the Enum and its corresponding values.
    private static final Map<String, EventStates> ENUM_MAP;
    //Variable to hold eventValue
    private final String eventValue;

    /**
     * Parametrised Constructor to States Enum class
     *
     * @param eventValue value of the States
     */
    EventStates(String eventValue) {
        this.eventValue = eventValue;
    }

    /**
     * Returns the Enum value for given String value.
     *
     * @param value String value for which corresponding Enum value need to be returned.
     * @return Enum value for given String value.
     */
    public static EventStates get(String value) {
        return ENUM_MAP.get(value);
    }

    /**
     * This will return the value assigned to the state
     *
     * @return Value assigned to the state
     */
    public String getEventValue() {
        return this.eventValue;
    }

    /**
     * Build an immutable map of String name to enum pairs.
     */
    static {
        final Map<String, EventStates> map = new ConcurrentHashMap<>();
        for (final EventStates instance : EventStates.values()) {
            map.put(instance.getEventValue(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

}
