
package com.philips.platform.flowmanager.pojo;

import com.philips.platform.appframework.flowmanager.FlowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppFlowNextState {

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
     * @return The nextState
     */
    public @FlowManager.AppState String getNextState() {
        return nextState;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
