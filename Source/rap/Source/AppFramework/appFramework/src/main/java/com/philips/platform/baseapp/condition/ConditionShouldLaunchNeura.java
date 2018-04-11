/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseCondition;


public class ConditionShouldLaunchNeura extends BaseCondition {


    /**
     * Constructor of BaseCondition
     *
     * @since 1.1.0
     */
    public ConditionShouldLaunchNeura() {
        super(AppConditions.SHOULD_LAUNCH_NEURA);
    }

    @Override
    public boolean isSatisfied(Context context) {
        return true;
    }
}
