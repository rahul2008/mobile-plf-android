package com.philips.cdp.wifirefuapp.states;

public class StateContext {
    private BaseState baseState;

    public void start() {
        baseState.start(this);
    }

    public void setState(BaseState baseState) {
        this.baseState = baseState;
    }

    public BaseState getState() {
        return baseState;
    }
}
