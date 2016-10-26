package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import com.philips.platform.flowmanager.AppFrameworkDataParser;
import com.philips.platform.flowmanager.condition.AppConditions;
import com.philips.platform.flowmanager.condition.BaseCondition;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.jsonstates.EventStates;
import com.philips.platform.flowmanager.pojo.AppFlowEvent;
import com.philips.platform.flowmanager.pojo.AppFlowModel;
import com.philips.platform.flowmanager.pojo.AppFlowNextState;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public abstract class BaseUiFlowManager {

    private Map<AppStates, List<AppFlowEvent>> mAppFlowMap;
    private Map<AppConditions, BaseCondition> appConditionsBaseConditionMap;
    private Map<AppStates, UIState> appStatesUIStateMap;
    private AppStates mFirstState;
    private AppStates currentState;
    private Context context;

    public BaseUiFlowManager(final Context context, @IdRes final int jsonPath) {
        this.context = context;
        appConditionsBaseConditionMap = new TreeMap<>();
        appStatesUIStateMap = new TreeMap<>();
        mapAppFlowStates(jsonPath);
        // TODO: Unless propositions see this code , they are not aware that when should they add these states .
        // we have put it in static blocks, propositions may not accept this path.
        addConditionMap(appConditionsBaseConditionMap);
        addStateMap(appStatesUIStateMap);
    }

    public abstract void addStateMap(Map<AppStates, UIState> appStatesUIStateMap);

    public abstract void addConditionMap(final Map<AppConditions, BaseCondition> appConditionsBaseConditionMap);

    /**
     * This method will creates and return the object of type of BaseUIState depending upon stateID
     *
     * @param state StateID for which the object need to be created
     * @return Objects 'BaseUIState' based on StateID
     */
    public UIState getUIStateState(AppStates state) {
        return appStatesUIStateMap.get(state);
    }

    /**
     * Method to get object of next UIState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next UIState if available or 'null'.
     */
    public UIState getNextState(AppStates currentState, EventStates event) {
        final List<AppFlowEvent> appFlowEvents = mAppFlowMap.get(currentState);
        if (appFlowEvents != null) {
            for (final AppFlowEvent appFlowEvent : appFlowEvents) {
                final EventStates eventStates = EventStates.get(appFlowEvent.getEventId());
                if (appFlowEvent.getEventId() != null && eventStates == event) {
                    final List<AppFlowNextState> appFlowNextStates = appFlowEvent.getNextStates();
                    UIState appFlowNextState = getUiState(appFlowNextStates);
                    if (appFlowNextState != null) return appFlowNextState;
                    break;
                }
            }
        }
        return null;
    }

    @Nullable
    private UIState getUiState(final List<AppFlowNextState> appFlowNextStates) {
        for (AppFlowNextState appFlowNextState : appFlowNextStates) {
            List<String> conditionsTypes = appFlowNextState.getCondition();
            boolean isConditionSatisfies = true;
            if (conditionsTypes != null && conditionsTypes.size() > 0) {
                isConditionSatisfies = isConditionSatisfies(conditionsTypes);
            }
            //Return the UIState if the entry condition is satisfies.
            if (isConditionSatisfies) {
                return getUIState(AppStates.get(appFlowNextState.getNextState()));
            }
        }
        return null;
    }

    private boolean isConditionSatisfies(final List<String> conditionsTypes) {
        boolean isConditionSatisfies = true;
        for (final String conditionType : conditionsTypes) {
            BaseCondition condition = getCondition(AppConditions.get(conditionType));
            isConditionSatisfies = condition.isConditionSatisfies(context);
            if (isConditionSatisfies)
                break;
        }
        return isConditionSatisfies;
    }

    /**
     * Method to return the Object to UIState based on AppFlowState ID.
     *
     * @param state state ID.
     * @return Object to UIState if available or 'null'.
     */
    private UIState getUIState(AppStates state) {
        UIState uiState = null;
        if (state != null) {
            uiState = getUIStateState(state);
        }
        return uiState;
    }

    public UIState getFirstState() {
        return getUIState(mFirstState);
    }

    /**
     * This method will creates and return the object of BaseCondition depending upon Condition ID.
     *
     * @param conditions Condition ID for which the BaseCondition type object need to be created.
     * @return Object of BaseCondition type.
     */
    public BaseCondition getCondition(AppConditions conditions) {

        return appConditionsBaseConditionMap.get(conditions);
    }

    private void mapAppFlowStates(@IdRes final int jsonPath) {
        final AppFlowModel appFlowModel = AppFrameworkDataParser.getAppFlow(context, jsonPath);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            mFirstState = AppStates.get(appFlowModel.getAppFlow().getFirstState());
            setCurrentState(mFirstState);
            mAppFlowMap = AppFrameworkDataParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
    }

    public AppStates getCurrentState() {
        return currentState;
    }

    public void setCurrentState(final AppStates currentState) {
        this.currentState = currentState;
    }
}
