package com.philips.cdp.appframework.statemachine;

import java.util.Objects;

/**
 * Created by 310240027 on 6/14/2016.
 */
public abstract class TriggerBase {
    int componentID;
    Object event;
    Object action;

    abstract void addTriggerState(int componentID, Object event, Objects action);
}
