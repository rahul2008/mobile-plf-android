package com.philips.platform.flowmanager.condition;

import android.content.Context;

import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConditionIsDonePressed implements BaseCondition {
    @Override
    public boolean isConditionSatisfies(final Context context) {
        final SharedPreferenceUtility sharedPreferenceUtility = new SharedPreferenceUtility(context);
        return sharedPreferenceUtility.getPreferenceBoolean(Constants.DONE_PRESSED);
    }
}
