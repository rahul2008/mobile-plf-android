package com.philips.cdp.appframework.statemachine;

import java.util.List;

/**
 * Created by 310240027 on 6/14/2016.
 */
public class UIFlowManager {
    List<StateBase> stateBaseList;

    public static StateBase getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(StateBase currentState) {
        UIFlowManager.currentState = currentState;
    }

    static StateBase currentState;

    public void addTrigger(TriggerBase base){

    }
    /*public void addState(StateBase state){

        stateBaseList.add(state);

    }

    public StateBase getState(StateBase state){
        for (StateBase stateBase:stateBaseList) {
            if(state.equals(stateBase)){
                return stateBase;
            }
        }
        return null;
    }*/



}
