
package com.philips.platform.flowmanager.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFlowState {

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
     * @param state The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return The events
     */
    public List<AppFlowEvent> getEvents() {
        return events;
    }

    /**
     * @param events The events
     */
    public void setEvents(List<AppFlowEvent> events) {
        this.events = events;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
