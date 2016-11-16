package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.flowmanager.AppFrameworkDataParser;
import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.pojo.AppFlowEvent;
import com.philips.platform.flowmanager.pojo.AppFlowModel;
import com.philips.platform.flowmanager.pojo.AppFlowNextState;

import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public abstract class BaseUiFlowManager {

    protected BaseAppCondition baseAppCondition;
    private Map<String, List<AppFlowEvent>> appFlowMap;
    private Context context;
    private AppFlowModel appFlowModel;
    private List<AppFlowEvent> appFlowEvents;
    protected Map<String, BaseState> stateMap;

    protected Map<String, BaseCondition> conditionMap;


    /**
     * This method will creates and return the object of BaseCondition depending upon Condition ID.
     *
     * @param key Condition ID for which the BaseCondition type object need to be created.
     * @return Object of BaseCondition type.
     */
    public BaseCondition getCondition(String key) {
        return conditionMap.get(key);
    }

    public abstract void populateStateMap(final Map<String, BaseState> uiStateMap);

    public abstract void populateConditionMap(final Map<String, BaseCondition> baseConditionMap);

    /**
     * Method to return the Object to BaseState based on AppFlowState ID.
     *
     * @param key state ID.
     * @return Object to BaseState if available or 'null'.
     */
    public BaseState getState(String key) {
        return stateMap.get(key);
    }

    public BaseUiFlowManager(final Context context, @IdRes final int jsonPath) {
        this.context = context;
        mapAppFlowStates(jsonPath);
        stateMap = new ArrayMap<>();
        conditionMap = new ArrayMap<>();
        populateStateMap(stateMap);
        populateConditionMap(conditionMap);
    }

    /**
     * Method to get object of next BaseState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next BaseState if available or 'null'.
     */
    public BaseState getNextState(String currentState, String event) {
        String string;
        if(null != currentState && null != event) {
            appFlowEvents = getAppFlowEvents(currentState);
            if (appFlowEvents != null) {
                for (final AppFlowEvent appFlowEvent : appFlowEvents) {
                    string = appFlowEvent.getEventId();
                    if (appFlowEvent.getEventId() != null && string.equals(event)) {
                        final List<AppFlowNextState> appFlowNextStates = appFlowEvent.getNextStates();
                        BaseState appFlowNextState = getUiState(appFlowNextStates);
                        if (appFlowNextState != null) return appFlowNextState;
                        break;
                    }
                }
            }
        }
        else {
            return getFirstState();
        }
        return null;
    }

    public List<AppFlowEvent> getAppFlowEvents(String currentState) {
        return appFlowMap.get(currentState);
    }

    @Nullable
    private BaseState getUiState(final List<AppFlowNextState> appFlowNextStates) {
        for (AppFlowNextState appFlowNextState : appFlowNextStates) {
            List<java.lang.String> conditionsTypes = appFlowNextState.getCondition();
            boolean isConditionSatisfies = true;
            if (conditionsTypes != null && conditionsTypes.size() > 0) {
                isConditionSatisfies = isConditionSatisfies(conditionsTypes);
            }
            //Return the BaseState if the entry condition is satisfies.
            if (isConditionSatisfies) {
                return getState(appFlowNextState.getNextState());
            }
        }
        return null;
    }

    private boolean isConditionSatisfies(final List<java.lang.String> conditionsTypes) {
        boolean isConditionSatisfies = true;
        for (final String conditionType : conditionsTypes) {
            BaseCondition condition = getCondition(conditionType);
            isConditionSatisfies = condition.isSatisfied(context);
            if (isConditionSatisfies)
                break;
        }
        return isConditionSatisfies;
    }

    private void mapAppFlowStates(@IdRes final int jsonPath) {
       appFlowModel = AppFrameworkDataParser.getAppFlow(context, jsonPath);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            appFlowMap = AppFrameworkDataParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
    }

    public final BaseState getFirstState() {
        return getState(appFlowModel.getAppFlow().getFirstState());
    }
}
