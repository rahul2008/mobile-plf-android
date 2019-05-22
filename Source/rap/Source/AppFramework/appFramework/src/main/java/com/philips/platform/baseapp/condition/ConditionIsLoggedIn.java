/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;

public class ConditionIsLoggedIn extends BaseCondition {
    public final String TAG = ConditionIsLoggedIn.class.getSimpleName();

    public ConditionIsLoggedIn() {
        super(AppConditions.IS_LOGGED_IN);
    }

    @Override
    public boolean isSatisfied(Context context) {
        RALog.d(TAG," isSatisfied called");

        return isUserSignIn(context);
    }

    protected boolean isUserSignIn(Context context) {
        RALog.d(TAG," isUserSignIn called");
        return ((AppFrameworkApplication)context.getApplicationContext()).getUserRegistrationState().getUserDataInterface().getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN;
    }
}
