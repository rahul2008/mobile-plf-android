/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.flowmanagerjson.condition;

import android.content.Context;

import com.philips.cdp.registration.User;

/**
 * Project           : Lumea
 * File Name         : ConditionIsLoggedIn
 * Description       : Condition Class to check if the user is logged in or not.
 * Revision History: version 1:
 * Date: 7/12/2016
 * Original author: Bhanu Hirawat
 * Description: Initial version
 */
public class ConditionIsLoggedIn implements BaseCondition {
    @Override
    public boolean isConditionSatisfies(Context context) {
        return new User(context).isUserSignIn();
    }
}
