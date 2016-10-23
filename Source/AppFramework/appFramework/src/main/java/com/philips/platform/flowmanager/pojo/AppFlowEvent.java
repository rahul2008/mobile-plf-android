
package com.philips.platform.flowmanager.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFlowEvent {

    private String eventId;
    private List<AppFlowNextState> appFlowNextStates = new ArrayList<AppFlowNextState>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId The eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return The appFlowNextStates
     */
    public List<AppFlowNextState> getAppFlowNextStates() {
        return appFlowNextStates;
    }

    /**
     * @param appFlowNextStates The appFlowNextStates
     */
    public void setAppFlowNextStates(List<AppFlowNextState> appFlowNextStates) {
        this.appFlowNextStates = appFlowNextStates;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
