/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseCondition;

public class ConditionIsLoggedIn extends BaseCondition {

    public ConditionIsLoggedIn() {
        super(AppConditions.IS_LOGGED_IN);
    }

    @Override
    public boolean isSatisfied(Context context) {
        return isUserSignIn(context);
    }

    protected boolean isUserSignIn(Context context) {
        return new User(context).isUserSignIn();
    }
}
