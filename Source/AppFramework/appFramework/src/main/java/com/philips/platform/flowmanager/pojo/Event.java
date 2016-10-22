
package com.philips.platform.flowmanager.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event {

    private String eventId;
    private List<NextState> nextStates = new ArrayList<NextState>();
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
    public List<NextState> getNextStates() {
        return nextStates;
    }

    /**
     * @param nextStates The nextStates
     */
    public void setNextStates(List<NextState> nextStates) {
        this.nextStates = nextStates;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
