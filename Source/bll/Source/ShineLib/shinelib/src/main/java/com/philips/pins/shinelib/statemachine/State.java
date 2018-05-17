package com.philips.pins.shinelib.statemachine;

import android.support.annotation.NonNull;

public abstract class State<T extends StateMachine> {

    @NonNull
    protected  T stateMachine;

    public State(@NonNull T stateMachine) {
        this.stateMachine = stateMachine;
    }

    protected abstract void onEnter();

    protected abstract void onExit();
}