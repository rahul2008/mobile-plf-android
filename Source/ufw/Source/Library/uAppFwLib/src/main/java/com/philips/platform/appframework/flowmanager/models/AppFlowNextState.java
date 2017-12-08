/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppFlowNextState {

    private List<String> condition = new ArrayList<String>();
    private String nextState;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The condition
     */
    public List<String> getCondition() {
        return condition;
    }

    /**
     * @return The nextState
     */
    public String getNextState() {
        return nextState;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
