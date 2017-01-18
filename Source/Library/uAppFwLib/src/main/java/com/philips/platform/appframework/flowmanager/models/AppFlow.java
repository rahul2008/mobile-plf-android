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

public class AppFlow {

    private String firstState;
    private List<AppFlowState> states = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The firstState
     */
    public String getFirstState() {
        return firstState;
    }

    /**
     *
     * @return
     *     The states
     */
    public List<AppFlowState> getStates() {
        return states;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
