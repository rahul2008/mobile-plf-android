
package com.philips.platform.flowmanager.pojo;

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
     * @param firstState
     *     The firstState
     */
    private void setFirstState(String firstState) {
        this.firstState = firstState;
    }

    /**
     *
     * @return
     *     The states
     */
    public List<AppFlowState> getStates() {
        return states;
    }

    /**
     *
     * @param states
     *     The states
     */
    public void setStates(List<AppFlowState> states) {
        this.states = states;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
