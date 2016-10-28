package com.philips.platform.flowmanager.condition;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.modularui.statecontroller.BaseAppCondition;

public class ConditionAppLaunch implements BaseCondition {
    @Override
    public boolean isConditionSatisfies(final Context context) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) context;
        final FlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        final boolean isUserLoggedIn = targetFlowManager.getCondition(BaseAppCondition.IS_LOGGED_IN).isConditionSatisfies(context);
        final boolean isDonePressed = targetFlowManager.getCondition(BaseAppCondition.IS_DONE_PRESSED).isConditionSatisfies(context);
        return isDonePressed && !isUserLoggedIn;
    }
}
