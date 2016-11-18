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

public final class AppFlowState {

    private String state;
    private List<AppFlowEvent> events = new ArrayList<AppFlowEvent>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The state
     */
    public String getState() {
        return state;
    }

    /**
     * @return The events
     */
    public List<AppFlowEvent> getEvents() {
        return events;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
