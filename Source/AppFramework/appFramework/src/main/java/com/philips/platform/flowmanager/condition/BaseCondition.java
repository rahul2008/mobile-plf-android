/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanager.condition;

import android.content.Context;

/**
 * Project           : Lumea
 * File Name         : BaseCondition
 * Description       : Base class for all condition type
 * Revision History: version 1:
 * Date: 7/12/2016
 * Original author: Bhanu Hirawat
 * Description: Initial version
 */
public interface BaseCondition {

    /**
     * This Method will return true if the condition satisfies otherwise return false.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @return 'true' if the condition satisfies otherwise return false.
     */
    boolean isConditionSatisfies(Context context);
}
