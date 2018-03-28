package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNDevice;

public class ReadyState extends State {

    public ReadyState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void setup() {

    }

    @Override
    public void breakdown() {

    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connected;
    }

    @Override
    public void disconnect() {
        stateMachine.setState(this, new DisconnectingState(stateMachine));
    }
}
