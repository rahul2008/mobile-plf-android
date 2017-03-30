/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.flowmanager.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.flowmanager.UappConditions;
import com.philips.platform.flowmanager.utility.UappConstants;
import com.philips.platform.flowmanager.utility.UappSharedPreference;

public class UappConditionIsDonePressed extends BaseCondition {

    public UappConditionIsDonePressed() {
        super(UappConditions.IS_DONE_PRESSED);
    }

    @Override
    public boolean isSatisfied(final Context context) {
        final UappSharedPreference uappSharedPreference = new UappSharedPreference(context);
        return uappSharedPreference.getPreferenceBoolean(UappConstants.DONE_PRESSED);
    }
}
