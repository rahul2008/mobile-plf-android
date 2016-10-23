
package com.philips.platform.flowmanager.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFlowNextState {

    private List<String> condition = new ArrayList<String>();
    private String nextState;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The condition
     */
    public List<String> getCondition() {
        return condition;
    }

    /**
     * @param condition The condition
     */
    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    /**
     * @return The nextState
     */
    public String getNextState() {
        return nextState;
    }

    /**
     * @param nextState The nextState
     */
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
