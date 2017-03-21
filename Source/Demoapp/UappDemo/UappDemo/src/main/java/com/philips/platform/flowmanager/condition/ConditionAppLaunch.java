/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.flowmanager.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.flowmanager.AppConditions;


public class ConditionAppLaunch extends BaseCondition {

    public ConditionAppLaunch() {
        super(AppConditions.CONDITION_APP_LAUNCH);
    }

    @Override
    public boolean isSatisfied(final Context context) {
        return true;
    }
}
