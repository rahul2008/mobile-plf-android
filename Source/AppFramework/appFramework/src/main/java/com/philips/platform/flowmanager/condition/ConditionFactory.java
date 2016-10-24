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
 * File Name         : CondtionFactory
 * Description       : Factory class to provide the object of BaseCondition.
 * Revision History: version 1:
 * Date: 7/12/2016
 * Original author: Bhanu Hirawat
 * Description: Initial version
 */
public class ConditionFactory {

    //Map to hold the Enum and its corresponding values.
    private static final Map<AppConditions, BaseCondition> CONDITIONS_MAP;

    /**
     * This method will creates and return the object of BaseCondition depending upon Condition ID.
     *
     * @param conditions Condition ID for which the BaseCondition type object need to be created.
     * @return Object of BaseCondition type.
     */
    public BaseCondition getCondition(AppConditions conditions) {

        return CONDITIONS_MAP.get(conditions);
    }

    /**
     * Build an immutable map of Condition type and corresponding class object pairs.
     */
    static {
        final Map<AppConditions, BaseCondition> map = new ConcurrentHashMap<AppConditions, BaseCondition>();
        map.put(AppConditions.IS_LOGGED_IN, new ConditionIsLoggedIn());
        map.put(AppConditions.IS_DONE_PRESSED, new ConditionIsDonePressed());
        CONDITIONS_MAP = Collections.unmodifiableMap(map);
    }
}
