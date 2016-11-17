/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.philips.platform.appframework.flowmanager.parser.AppFrameworkDataParser;
import com.philips.platform.appframework.flowmanager.pojo.AppFlowEvent;
import com.philips.platform.appframework.flowmanager.pojo.AppFlowModel;
import com.philips.platform.appframework.flowmanager.pojo.AppFlowNextState;

import java.util.List;
import java.util.Map;

public abstract class BaseUiFlowManager {

    private Map<String, List<AppFlowEvent>> appFlowMap;
    private BaseState currentState;
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
    // TODO: Deepthi , enough validation is not done here to check params and return type, make sure enough test cases are added.
    // put @nonNull for all public APIs and make sure it behaves properly when they send null values and app does not crash.
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
    // TODO: Deepthi , enough validation is not done here to check params and return type, make sure enough test cases are added.
    public BaseState getState(String key) {
        return stateMap.get(key);
    }

    // TODO: Deepthi we need to change to string
    // TODO: Deepthi please rename to  AppFlow.json
    public BaseUiFlowManager(final Context context, @IdRes final int jsonPath) {
        this.context = context;
        mapAppFlowStates(jsonPath);
        stateMap = new ArrayMap<>();
        conditionMap = new ArrayMap<>();
        populateStateMap(stateMap);
        populateConditionMap(conditionMap);
    }


    public BaseState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BaseState currentState) {
        this.currentState = currentState;
    }
    /**
     * Method to get object of next BaseState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next BaseState if available or 'null'.
     */
    // TODO: Deepthi make it eventId
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
                        if (appFlowNextState != null)
                        {
                            setCurrentState(appFlowNextState);
                            return appFlowNextState;
                        }

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

    // TODO: Deepthi change to string
    private void mapAppFlowStates(@IdRes final int jsonPath) {
       appFlowModel = AppFrameworkDataParser.getAppFlow(context, jsonPath);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            appFlowMap = AppFrameworkDataParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
    }

    private final BaseState getFirstState() {
        return getState(appFlowModel.getAppFlow().getFirstState());
    }
}
