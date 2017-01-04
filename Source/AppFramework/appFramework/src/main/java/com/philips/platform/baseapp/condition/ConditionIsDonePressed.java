/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;

import philips.appframeworklibrary.flowmanager.base.BaseCondition;

public class ConditionIsDonePressed extends BaseCondition {

    public ConditionIsDonePressed() {
        super(AppConditions.IS_DONE_PRESSED);
    }

    @Override
    public boolean isSatisfied(final Context context) {
        final SharedPreferenceUtility sharedPreferenceUtility = new SharedPreferenceUtility(context);
        return sharedPreferenceUtility.getPreferenceBoolean(Constants.DONE_PRESSED);
    }
}
