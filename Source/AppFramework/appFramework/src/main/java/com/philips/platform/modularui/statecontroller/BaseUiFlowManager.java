package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

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

    protected BaseAppState baseAppState;
    protected BaseAppCondition baseAppCondition;
    private Map<String, List<AppFlowEvent>> mAppFlowMap;
    private Context context;
    private String mFirstState;

    public BaseUiFlowManager(final Context context, @IdRes final int jsonPath) {
        this.context = context;
        mapAppFlowStates(jsonPath);
    }

    /**
     * This method will creates and return the object of type of BaseUIState depending upon stateID
     *
     * @param state StateID for which the object need to be created
     * @return Objects 'BaseUIState' based on StateID
     */
    private BaseState getUIStateState(String state) {
        return baseAppState.getState(state);
    }

    /**
     * Method to get object of next BaseState based on the current state of the App.
     *
     * @param currentState current state of the app.
     * @return Object to next BaseState if available or 'null'.
     */
    public final BaseState getNextState(String currentState, String event) {
        final List<AppFlowEvent> appFlowEvents = mAppFlowMap.get(currentState);
        if (appFlowEvents != null) {
            for (final AppFlowEvent appFlowEvent : appFlowEvents) {
                final String string = appFlowEvent.getEventId();
                if (appFlowEvent.getEventId() != null && string.equals(event)) {
                    final List<AppFlowNextState> appFlowNextStates = appFlowEvent.getNextStates();
                    BaseState appFlowNextState = getUiState(appFlowNextStates);
                    if (appFlowNextState != null) return appFlowNextState;
                    break;
                }
            }
        }
        return null;
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
                return getUIState(appFlowNextState.getNextState());
            }
        }
        return null;
    }

    private boolean isConditionSatisfies(final List<java.lang.String> conditionsTypes) {
        boolean isConditionSatisfies = true;
        for (final String conditionType : conditionsTypes) {
            BaseCondition condition = getCondition(conditionType);
            isConditionSatisfies = condition.isConditionSatisfies(context);
            if (isConditionSatisfies)
                break;
        }
        return isConditionSatisfies;
    }

    /**
     * This method will creates and return the object of BaseCondition depending upon Condition ID.
     *
     * @param condition Condition ID for which the BaseCondition type object need to be created.
     * @return Object of BaseCondition type.
     */
    private BaseCondition getCondition(String condition) {
        return baseAppCondition.getCondition(condition);
    }


    /**
     * Method to return the Object to BaseState based on AppFlowState ID.
     *
     * @param state state ID.
     * @return Object to BaseState if available or 'null'.
     */
    private BaseState getUIState(String state) {
        BaseState baseState = null;
        if (state != null) {
            baseState = getUIStateState(state);
        }
        return baseState;
    }

    private void mapAppFlowStates(@IdRes final int jsonPath) {
        final AppFlowModel appFlowModel = AppFrameworkDataParser.getAppFlow(context, jsonPath);
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            mAppFlowMap = AppFrameworkDataParser.getAppFlowMap(appFlowModel.getAppFlow());
            mFirstState = appFlowModel.getAppFlow().getFirstState();
        }
    }

    public final BaseState getFirstState() {
        return getUIState(mFirstState);
    }
}
