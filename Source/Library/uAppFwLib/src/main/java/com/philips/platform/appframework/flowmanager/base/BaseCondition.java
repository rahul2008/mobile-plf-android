/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;

public abstract class BaseCondition {

    private String conditionID;

    public BaseCondition(String conditionID){
        this.conditionID = conditionID;
    }


    public String getConditionID() {
        return conditionID;
    }

    /**
     * This Method will return true if the condition satisfies otherwise return false.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @return 'true' if the condition satisfies otherwise return false.
     */
    public abstract boolean isSatisfied(Context context);
}
