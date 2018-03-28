package com.philips.pins.shinelib.statemachine;

public interface StateChangedListener<T extends State> {

    void onStateChanged(T oldState, T newState);
}
