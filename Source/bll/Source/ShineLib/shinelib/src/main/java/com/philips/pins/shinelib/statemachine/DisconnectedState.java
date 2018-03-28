package com.philips.pins.shinelib.statemachine;

import com.philips.pins.shinelib.SHNDevice;

public class DisconnectedState extends SHNDeviceState {

    public DisconnectedState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {
        sharedResources.setLastDisconnectedTimeMillis(System.currentTimeMillis());
        sharedResources.getShnCentral().unregisterSHNCentralStatusListenerForAddress(sharedResources.getShnCentralListener(), sharedResources.getBtDevice().getAddress());
    }

    @Override
    protected void onExit() {

    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Disconnected;
    }

    @Override
    public void connect() {
        stateMachine.setState(this, new GattConnectingState(stateMachine, sharedResources));
    }

    @Override
    public void connect(long connectTimeOut) {
        stateMachine.setState(this, new GattConnectingState(stateMachine, sharedResources, connectTimeOut));
    }

    @Override
    public void connect(final boolean withTimeout, final long timeoutInMS) {
        stateMachine.setState(this, new GattConnectingState(stateMachine, sharedResources, withTimeout, timeoutInMS));
    }

    @Override
    public void disconnect() {
        sharedResources.notifyStateToListener();
    }
}
