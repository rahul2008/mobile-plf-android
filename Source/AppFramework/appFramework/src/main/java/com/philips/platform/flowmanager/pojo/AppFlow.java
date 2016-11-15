
package com.philips.platform.flowmanager.pojo;

import com.philips.platform.appframework.flowmanager.FlowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppFlow {

    private String firstState;
    private List<AppFlowState> states = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The firstState
     */
    public @FlowManager.AppState String getFirstState() {
        return firstState;
    }

    /**
     *
     * @return
     *     The states
     */
    public List<AppFlowState> getStates() {
        return states;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
