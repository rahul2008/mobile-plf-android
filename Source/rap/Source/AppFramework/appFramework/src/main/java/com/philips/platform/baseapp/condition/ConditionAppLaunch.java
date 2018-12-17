/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;

public class ConditionAppLaunch extends BaseCondition {
    public final String TAG = ConditionAppLaunch.class.getSimpleName();

    public ConditionAppLaunch() {
        super(AppConditions.CONDITION_APP_LAUNCH);
    }

    @Override
    public boolean isSatisfied(final Context context) {
        RALog.d(TAG," isSatisfied called");
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) context;
        final BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        final boolean isUserLoggedIn = targetFlowManager.getCondition(AppConditions.IS_LOGGED_IN).isSatisfied(context);
        final boolean isDonePressed = targetFlowManager.getCondition(AppConditions.IS_DONE_PRESSED).isSatisfied(context);
        return isDonePressed && !isUserLoggedIn;
    }
}
