/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonStructureException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.NullEventException;
import com.philips.platform.appframework.flowmanager.listeners.AppFlowJsonListener;
import com.philips.platform.appframework.flowmanager.models.AppFlowEvent;
import com.philips.platform.appframework.flowmanager.models.AppFlowModel;
import com.philips.platform.appframework.flowmanager.models.AppFlowNextState;
import com.philips.platform.appframework.flowmanager.stack.FlowManagerStack;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class BaseFlowManager {

    private Map<String, BaseState> stateMap;
    private Map<String, BaseCondition> conditionMap;
    private Map<String, List<AppFlowEvent>> appFlowMap;
    private BaseState currentState;
    private Context context;
    private String firstState;
    private FlowManagerStack flowManagerStack;
    private String BACK = "back";

    public BaseFlowManager(final Context context, final String jsonPath) {
        initFlowManager(context, jsonPath, null);
    }

    protected BaseFlowManager(String data, AppFlowJsonListener appFlowJsonListener) {
        mapAppFlowForTestCase(data, appFlowJsonListener);
        flowManagerStack = new FlowManagerStack();
        stateMap = new TreeMap<>();
        conditionMap = new TreeMap<>();
        populateStateMap(stateMap);
        populateConditionMap(conditionMap);
    }

    public void initFlowManager(final Context context, final String jsonPath, final AppFlowJsonListener appFlowJsonListener) throws JsonFileNotFoundException, JsonStructureException {
        this.context = context;
        mapAppFlowStates(jsonPath, appFlowJsonListener);
        flowManagerStack = new FlowManagerStack();
        stateMap = new TreeMap<>();
        conditionMap = new TreeMap<>();
        populateStateMap(stateMap);
        populateConditionMap(conditionMap);
    }
    /**
     * This method will create and return the object of BaseCondition depending on Condition ID.
     *
     * @param conditionId Condition ID for which the BaseCondition object needs to be created.
     * @return Object of BaseCondition type.
     */
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
    public BaseState getNextState(BaseState currentState, String eventId) throws NoEventFoundException, NoStateException, NoConditionFoundException {
        setCurrentState(currentState);
        return getNextState(eventId);
    }

    @NonNull
    public BaseState getNextState(String eventId) throws NoEventFoundException, NoStateException, NoConditionFoundException {
        if (null == eventId)
            throw new NullEventException();
        else if (null != currentState) {
            List<AppFlowEvent> appFlowEvents = getAppFlowEvents(currentState.getStateID());
            BaseState appFlowNextState = getStateForEventID(false, eventId, appFlowEvents);
            if (appFlowNextState != null) {
                setCurrentState(appFlowNextState);
                flowManagerStack.push(appFlowNextState);
                return appFlowNextState;
            }
        }
        throw new NoStateException();
    }

    public BaseState getFirstState() throws NoStateException {
        BaseState firstState = stateMap.get(this.firstState);
        if (firstState != null) {
            setCurrentState(firstState);
            flowManagerStack.push(firstState);
            return firstState;
        }
        throw new NoStateException();
    }

    @Nullable
    private BaseState getStateForEventID(boolean isBack, String eventId, List<AppFlowEvent> appFlowEvents) throws NoEventFoundException, NoConditionFoundException {
        String appFlowEventId;
        if (appFlowEvents != null && appFlowEvents.size() != 0) {
            for (final AppFlowEvent appFlowEvent : appFlowEvents) {
                appFlowEventId = appFlowEvent.getEventId();
                if (appFlowEvent.getEventId() != null && appFlowEventId.equalsIgnoreCase(eventId)) {
                    final List<AppFlowNextState> appFlowNextStates = appFlowEvent.getNextStates();
                    return getUiState(appFlowNextStates);
                }
            }
        }
        if (!isBack)
            throw new NoEventFoundException();
        else
            return null;
    }

    public BaseState getBackState(BaseState currentState) throws NoStateException, NoConditionFoundException {
        return getBackState();
    }

    public BaseState getBackState() throws NoStateException, NoConditionFoundException {
        if (currentState != null && flowManagerStack.contains(currentState)) {
            List<AppFlowEvent> appFlowEvents = getAppFlowEvents(currentState.getStateID());
            BaseState nextState = null;
            try {
                nextState = getStateForEventID(true, BACK, appFlowEvents);
            } catch (NoEventFoundException e) {
                e.printStackTrace();
            }
            if (nextState != null) {
                if (flowManagerStack.contains(nextState)) {
                    flowManagerStack.pop(nextState);
                    setCurrentState(nextState);
                    return nextState;
                } else {
                    flowManagerStack.clear();
                    setCurrentState(nextState);
                    flowManagerStack.push(nextState);
                    return nextState;
                }
            } else {
                setCurrentState(flowManagerStack.pop());
                return null;
            }
        }
        throw new NoStateException();
    }

    public void clearStates() {
        flowManagerStack.clear();
    }

    private List<AppFlowEvent> getAppFlowEvents(String currentState) {
        return appFlowMap.get(currentState);
    }

    @Nullable
    private BaseState getUiState(final List<AppFlowNextState> appFlowNextStates) throws NoConditionFoundException {
        for (AppFlowNextState appFlowNextState : appFlowNextStates) {
            List<String> conditionsTypes = appFlowNextState.getCondition();
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

    private boolean isConditionSatisfies(final List<String> conditionsTypes) throws NoConditionFoundException {
        boolean isConditionSatisfies = true;
        for (final String conditionType : conditionsTypes) {
            BaseCondition condition = getCondition(conditionType);
            if (condition != null) {
                isConditionSatisfies = condition.isSatisfied(context);
                if (isConditionSatisfies)
                    break;
            } else {
                throw new NoConditionFoundException();
            }
        }
        return isConditionSatisfies;
    }

    private void mapAppFlowStates(final String jsonPath, AppFlowJsonListener appFlowJsonListener) throws JsonFileNotFoundException, JsonStructureException {
        final AppFlowParser appFlowParser = new AppFlowParser();
        AppFlowModel appFlowModel = appFlowParser.getAppFlow(jsonPath);
        getFirstStateAndAppFlowMap(appFlowParser, appFlowModel);
        if (appFlowJsonListener != null)
            appFlowJsonListener.onParseSuccess();
    }

    private void mapAppFlowForTestCase(final String data, AppFlowJsonListener appFlowJsonListener) throws JsonFileNotFoundException, JsonStructureException {
        final AppFlowParser appFlowParser = new AppFlowParser();
        AppFlowModel appFlowModel = appFlowParser.getAppFlowTestCase(data);
        getFirstStateAndAppFlowMap(appFlowParser, appFlowModel);
        if (appFlowJsonListener != null)
            appFlowJsonListener.onParseSuccess();
    }

    private void getFirstStateAndAppFlowMap(AppFlowParser appFlowParser, AppFlowModel appFlowModel) {
        if (appFlowModel != null && appFlowModel.getAppFlow() != null) {
            firstState = appFlowModel.getAppFlow().getFirstState();
            appFlowMap = appFlowParser.getAppFlowMap(appFlowModel.getAppFlow());
        }
    }
}
