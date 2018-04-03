package com.philips.pins.shinelib.statemachine.state;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;

public class SHNDisconnectedState extends SHNDeviceState {

    public SHNDisconnectedState(@NonNull SHNDeviceStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected void onEnter() {
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
        stateMachine.setState(new SHNGattConnectingState(stateMachine));
    }

    @Override
    public void connect(long connectTimeOut) {
        stateMachine.setState(new SHNGattConnectingState(stateMachine, connectTimeOut));
    }

    @Override
    public void disconnect() {
        sharedResources.notifyStateToListener();
    }
}
