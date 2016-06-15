package com.philips.cdp.appframework.statemachine;

import java.util.List;

/**
 * Created by 310240027 on 6/14/2016.
 */
public class UIFlowManager {
    List<StateBase> stateBaseList;
    public void addState(StateBase state){

        stateBaseList.add(state);

    }

    public StateBase getState(StateBase state){
        for (StateBase stateBase:stateBaseList) {
            if(state.equals(stateBase)){
                return stateBase;
            }
        }
        return null;
    }
}
