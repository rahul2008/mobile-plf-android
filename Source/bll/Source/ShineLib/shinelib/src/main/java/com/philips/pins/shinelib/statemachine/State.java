package com.philips.pins.shinelib.statemachine;

public abstract class State<T extends StateMachine> {

    protected  T stateMachine;

    public State(T stateMachine) {
        this.stateMachine = stateMachine;
    }

    protected abstract void onEnter();

    protected abstract void onExit();
}