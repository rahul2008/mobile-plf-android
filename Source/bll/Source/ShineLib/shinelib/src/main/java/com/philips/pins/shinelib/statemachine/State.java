package com.philips.pins.shinelib.statemachine;

public abstract class State<T> {

    protected  StateMachine stateMachine;
    protected T sharedResources;

    public State(StateMachine stateMachine, T sharedResources) {
        this.stateMachine = stateMachine;
        this.sharedResources = sharedResources;
    }

    protected abstract void onEnter();

    protected abstract void onExit();
}