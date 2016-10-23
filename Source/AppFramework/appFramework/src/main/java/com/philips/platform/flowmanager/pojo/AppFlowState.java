
package com.philips.platform.flowmanager.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFlowState {

    private String state;
    private List<AppFlowEvent> appFlowEvents = new ArrayList<AppFlowEvent>();
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
     * @return The appFlowEvents
     */
    public List<AppFlowEvent> getAppFlowEvents() {
        return appFlowEvents;
    }

    /**
     * @param appFlowEvents The appFlowEvents
     */
    public void setAppFlowEvents(List<AppFlowEvent> appFlowEvents) {
        this.appFlowEvents = appFlowEvents;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
