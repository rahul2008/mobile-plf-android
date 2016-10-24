
package com.philips.platform.flowmanager.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFlowEvent {

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
     * @param eventId The eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return The nextStates
     */
    public List<AppFlowNextState> getNextStates() {
        return nextStates;
    }

    /**
     * @param nextStates The nextStates
     */
    public void setNextStates(List<AppFlowNextState> nextStates) {
        this.nextStates = nextStates;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
