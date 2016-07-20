package com.philips.platform.modularui.statecontroller;

import android.content.Context;

import com.philips.platform.modularui.factorymanager.StateCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310240027 on 7/6/2016.
 */
public class FlowManager {

    Map<Integer, UIState> stateMap;
    UIState currentState;

    public UIState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(UIState currentState) {
        this.currentState = currentState;
    }

    public FlowManager(){
        stateMap = new HashMap<Integer, UIState>();
    }

    public Map<Integer, UIState> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<Integer, UIState> stateMap) {
        this.stateMap = stateMap;
    }

    public void getNextState(){

    }
    public void navigateState(@UIStateBase.UIStateDef int stateID, Context context) {
            StateCreator.getInstance().getState(stateID,context).getNavigator().navigate(context);
            setCurrentState(StateCreator.getInstance().getState(stateID,context));
    }

    public void addToStateMap(UIState uiState) {
        stateMap.put(uiState.getStateID(), uiState);
    }

    public UIStateBase getState(@UIStateBase.UIStateDef int stateID) {
        return stateMap.get(stateID);
    }
}
