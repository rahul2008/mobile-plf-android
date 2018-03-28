package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNDevice;

public class DisconnectedState extends State {

    public DisconnectedState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void setup() {
        sharedResources.setLastDisconnectedTimeMillis(System.currentTimeMillis());
        sharedResources.getShnCentral().unregisterSHNCentralStatusListenerForAddress(sharedResources.getShnCentralListener(), sharedResources.getBtDevice().getAddress());
    }

    @Override
    public void breakdown() {

    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Disconnected;
    }

    @Override
    public void connect() {
        stateMachine.setState(this, new GattConnectingState(stateMachine));
    }

    @Override
    public void connect(long connectTimeOut) {
        stateMachine.setState(this, new GattConnectingState(stateMachine, connectTimeOut));
    }

    @Override
    public void connect(final boolean withTimeout, final long timeoutInMS) {
        stateMachine.setState(this, new GattConnectingState(stateMachine, withTimeout, timeoutInMS));
    }
}
