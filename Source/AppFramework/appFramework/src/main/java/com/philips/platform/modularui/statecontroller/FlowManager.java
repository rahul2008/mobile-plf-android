package com.philips.platform.modularui.statecontroller;

import android.content.Context;

import com.philips.platform.modularui.eventbus.StateEvent;
import com.philips.platform.modularui.factorymanager.StateCreator;
import com.philips.platform.modularui.util.UIConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310240027 on 7/6/2016.
 */
public class FlowManager {

    StateCreator stateCreator;
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
        stateCreator = new StateCreator();
    }

    public Map<Integer, UIState> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<Integer, UIState> stateMap) {
        this.stateMap = stateMap;
    }

    public void getNextState(){

    }
    //TODO : optimize hashmap creation to avoid multiple instances of navigator getting creator.
    public void navigateState(@UIConstants.UIStateDef int stateID, Context context) {
        if (getStateMap().containsKey(stateID)) {
            //TODO: update current state here
                getStateMap().get(stateID).getNavigator().navigate(context);
        }else {
            EventBus.getDefault().post(new StateEvent(stateID,context));
        }
    }

    public void addToStateMap(UIState uiState) {
        stateMap.put(uiState.getStateID(), uiState);
    }

    public UIStateBase getState(@UIConstants.UIStateDef int stateID) {
        return stateMap.get(stateID);
    }
}
