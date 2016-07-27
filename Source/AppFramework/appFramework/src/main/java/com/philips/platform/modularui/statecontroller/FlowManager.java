package com.philips.platform.modularui.statecontroller;

import android.content.Context;

import com.philips.platform.modularui.factorymanager.StateCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310240027 on 7/6/2016.
 */
//TODO: UIFlowManager rename
public class FlowManager {
    // TODO: Array of state objects
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

    // TODO: remove this
    public void setStateMap(Map<Integer, UIState> stateMap) {
        this.stateMap = stateMap;
    }

    // TODO: remove this
    public void getNextState(){

    }

    // TODO: Presenter should be represented as an interface
    public void navigateToState(@UIState.UIStateDef int stateID, Context context, UIBasePresenter uiBasePresenter) {
        // TODO: use local variable to hold state object created by state creator
            setCurrentState(StateCreator.getInstance().getState(stateID,context));
            StateCreator.getInstance().getState(stateID,context).setPresenter(uiBasePresenter);
            StateCreator.getInstance().getState(stateID,context).navigate(context);
    }

    // TODO: can it become private method?
    public void addToStateMap(UIState uiState) {
        stateMap.put(uiState.getStateID(), uiState);
    }

    public UIState getState(@UIState.UIStateDef int stateID) {
        return stateMap.get(stateID);
    }
}
