package com.philips.cdp.appframework.statemachine;

/**
 * Created by 310240027 on 6/14/2016.
 */
public abstract class TriggerBase {
    @UIState.NavigationMode UIState uiState;
    int componentID;
    Object event;
    Object action;


}
