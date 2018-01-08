/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;

/**
 * This is the base class for all the conditions that needs to be used by the proposition. This is an abstract class. Any condition that is defined in the AppFlow.json will have create a corresponding class and extend from this base class. This ensures the FlowManager can access and check for this condition in getNextState internally.
 * @since 1.1.0
 */
public abstract class BaseCondition {

    private String conditionID;

    /**
     * Constructor of BaseCondition
     * @param conditionID The condition ID
     * @since 1.1.0
     */
    public BaseCondition(String conditionID){
        this.conditionID = conditionID;
    }

    /**
     * This API is used to get the condition ID for this Condition Object.
     * @return returns the condition ID
     * @since 1.1.0
     */
    public String getConditionID() {
        return conditionID;
    }

    /**
     * This Method will return true if the condition satisfies otherwise return false.
     *
     * @param context The context to use  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @return 'true' if the condition satisfies otherwise return false
     * @since 1.1.0
     */
    public abstract boolean isSatisfied(Context context);
}
