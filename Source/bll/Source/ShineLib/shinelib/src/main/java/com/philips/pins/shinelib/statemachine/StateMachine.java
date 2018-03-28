package com.philips.pins.shinelib.statemachine;

import android.support.annotation.NonNull;

public class StateMachine<T extends State> {

    private T state;
    private StateChangedListener<T> stateChangedListener;

    public StateMachine(StateChangedListener<T> stateChangedListener) {
        this.stateChangedListener = stateChangedListener;
    }

    public void setInitialState(T initialState) {
        if(this.state == null) {
            this.state = initialState;
        }
    }

    public synchronized void setState(@NonNull T oldState, @NonNull  T newState) {
        if(this.state != oldState || newState == null) {
            return;
        }

        this.state.onExit();
        this.state = newState;
        this.state.onEnter();

        this.stateChangedListener.onStateChanged(oldState, newState);
    }

    public T getState() {
        return state;
    }
}
