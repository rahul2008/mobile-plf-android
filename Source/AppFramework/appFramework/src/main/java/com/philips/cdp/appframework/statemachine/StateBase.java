package com.philips.cdp.appframework.statemachine;

import java.util.List;

/**
 * Created by 310240027 on 6/14/2016.
 */
public abstract class StateBase {
    UIState uiState;
    List<TriggerBase> triggerBaseList;

    abstract void handleState();
    abstract void handleBehaviour();
    abstract void setTrigger();
    abstract void getAction();



}
