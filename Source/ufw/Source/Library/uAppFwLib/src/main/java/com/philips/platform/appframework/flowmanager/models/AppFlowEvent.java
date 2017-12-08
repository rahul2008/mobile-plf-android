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

public final class AppFlowEvent {

    private String eventId;
    private List<AppFlowNextState> nextStates = new ArrayList<AppFlowNextState>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @return The nextStates
     */
    public List<AppFlowNextState> getNextStates() {
        return nextStates;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
