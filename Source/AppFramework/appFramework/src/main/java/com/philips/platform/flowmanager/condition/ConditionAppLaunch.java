/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.flowmanager.condition;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.modularui.statecontroller.BaseUiFlowManager;

public class ConditionAppLaunch extends BaseCondition {

    public ConditionAppLaunch() {
        super(AppConditions.CONDITION_APP_LAUNCH);
    }

    @Override
    public boolean isSatisfied(final Context context) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) context;
        final BaseUiFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        final boolean isUserLoggedIn = targetFlowManager.getCondition(AppConditions.IS_LOGGED_IN).isSatisfied(context);
        final boolean isDonePressed = targetFlowManager.getCondition(AppConditions.IS_DONE_PRESSED).isSatisfied(context);
        return isDonePressed && !isUserLoggedIn;
    }
}
