
package com.philips.platform.modularui.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appflow {

    private String firstState;
    private List<State> states = new ArrayList<State>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The firstState
     */
    public String getFirstState() {
        return firstState;
    }

    /**
     * @param firstState The firstState
     */
    public void setFirstState(String firstState) {
        this.firstState = firstState;
    }

    /**
     * @return The states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @param states The states
     */
    public void setStates(List<State> states) {
        this.states = states;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
