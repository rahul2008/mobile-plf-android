package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IdRes;

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

    private Context context;

    public abstract void addStateMap(Map<AppStates, UIState> appStatesUIStateMap);

    public abstract void addConditionMap(final Map<AppConditions, BaseCondition> appConditionsBaseConditionMap);

    private static Map<AppStates, List<AppFlowEvent>> mAppFlowMap;

    private static Map<AppConditions, BaseCondition> appConditionsBaseConditionMap;

    //Map to hold the Enum and its corresponding values.
    private static Map<AppStates, UIState> appStatesUIStateMap;

    //Object to hold first state of the app flow.
    private static AppStates mFirstState;

    private static AppStates currentState;

    public BaseUiFlowManager(final Context context, @IdRes final int jsonPath) {
        this.context = context;
        appConditionsBaseConditionMap = new TreeMap<>();
        appStatesUIStateMap = new TreeMap<>();
        mapAppFlowStates(jsonPath);
        addConditionMap(appConditionsBaseConditionMap);
        addStateMap(appStatesUIStateMap);
    }

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
        //Getting the list of all possible next state for the give 'currentState'.
        final List<AppFlowEvent> appFlowEvents = mAppFlowMap.get(currentState);

        if (appFlowEvents != null) {
            //Looping through all the possible next states and returning the state which satisfies
            // any entry condition.
            for (final AppFlowEvent appFlowEvent : appFlowEvents) {
                //boolean to hold the status of entry condition for the 'appFlowEvent'.

                final EventStates eventStates = EventStates.get(appFlowEvent.getEventId());
                if (appFlowEvent.getEventId() != null && eventStates == event) {
                    final List<AppFlowNextState> appFlowNextStates = appFlowEvent.getNextStates();
                    //Getting list of all possible entry conditions
                    for (AppFlowNextState appFlowNextState : appFlowNextStates) {
                        boolean isConditionSatisfies = true;
                        List<String> conditionsTypes = appFlowNextState.getCondition();
                        if (conditionsTypes != null && conditionsTypes.size() > 0) {
                            for (final String conditionType : conditionsTypes) {
                                BaseCondition condition = getCondition(AppConditions.get(conditionType));
                                isConditionSatisfies = condition.isConditionSatisfies(context);
                                if (isConditionSatisfies)
                                    break;
                            }
                        }
                        //Return the UIState if the entry condition is satisfies.
                        if (isConditionSatisfies) {
                            return getUIState(AppStates.get(appFlowNextState.getNextState()));
                        }
                    }
                    break;
                }
            }
        }
        return null;
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
        BaseUiFlowManager.currentState = currentState;
    }
}
