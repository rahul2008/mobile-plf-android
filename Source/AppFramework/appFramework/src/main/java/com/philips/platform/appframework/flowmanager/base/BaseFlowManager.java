/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.models.AppFlowEvent;
import com.philips.platform.appframework.flowmanager.models.AppFlowModel;
import com.philips.platform.appframework.flowmanager.models.AppFlowNextState;
import com.philips.platform.appframework.flowmanager.parser.AppFlowParser;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class BaseFlowManager {

    protected Map<String, BaseState> stateMap;
    protected Map<String, BaseCondition> conditionMap;
    private Map<String, List<AppFlowEvent>> appFlowMap;
    private BaseState currentState;
    private Context context;
    private AppFlowModel appFlowModel;
    private List<AppFlowEvent> appFlowEvents;

    // TODO: Deepthi we need to change to string
    public BaseFlowManager(final Context context, final String jsonPath) {
        this.context = context;
        mapAppFlowStates(jsonPath);
        stateMap = new TreeMap<>();
        conditionMap = new TreeMap<>();
        populateStateMap(stateMap);
        populateConditionMap(conditionMap);
    }

    /**
     * This method will creates and return the object of BaseCondition depending upon Condition ID.
     *
     * @param conditionId Condition ID for which the BaseCondition type object need to be created.
     * @return Object of BaseCondition type.
     */
    // TODO: Deepthi , enough validation is not done here to check params and return type, make sure enough test cases are added.
    // put @nonNull for all public APIs and make sure it behaves properly when they send null values and app does not crash.
    @NonNull
    public BaseCondition getCondition(String conditionId) {
        return conditionMap.get(conditionId);
    }

    public abstract void populateStateMap(final Map<String, BaseState> uiStateMap);

    public abstract void populateConditionMap(final Map<String, BaseCondition> baseConditionMap);

    /**
     * Method to return the Object to BaseState based on AppFlowState ID.
     *
     * @param stateId state ID.
     * @return Object to BaseState if available or 'null'.
     */
    // TODO: Deepthi , enough validation is not done here to check params and return type, make sure enough test cases are added.
    @NonNull
    public BaseState getState(String stateId) {
        return stateMap.get(stateId);
    }

    @NonNull
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
    public BaseState getNextState(BaseState currentState, String eventId) {
        String string;
        if (null != currentState && null != eventId) {
            appFlowEvents = getAppFlowEvents(currentState.getStateID());
            if (appFlowEvents != null) {
                for (final AppFlowEvent appFlowEvent : appFlowEvents) {
                    string = appFlowEvent.getEventId();
                    if (appFlowEvent.getEventId() != null && string.equals(eventId)) {
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
            return stateMap.get(AppStates.FIRST_STATE);
        }
        return null;
    }

    private List<AppFlowEvent> getAppFlowEvents(String currentState) {
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

    private void mapAppFlowStates(final String jsonPath) {
        appFlowModel = AppFlowParser.getAppFlow(jsonPath);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            appFlowMap = AppFlowParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
    }
}
