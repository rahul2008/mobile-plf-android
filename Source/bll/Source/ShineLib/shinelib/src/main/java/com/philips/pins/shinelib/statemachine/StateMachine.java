package com.philips.pins.shinelib.statemachine;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

public class StateMachine {

    private static final String TAG = StateMachine.class.getSimpleName();

    private State state;
    private SharedResources sharedResources;

    public StateMachine(SHNDevice shnDevice, BTDevice btDevice, SHNCentral shnCentral, String deviceTypeName, SHNDeviceImpl.SHNBondInitiator shnBondInitiator, SHNCentral.SHNCentralListener shnCentralListener, BTGatt.BTGattCallback btGattCallback) {
        this.sharedResources = new SharedResources(shnDevice, btDevice, shnCentral, deviceTypeName, shnBondInitiator, shnCentralListener ,btGattCallback);
        this.state = new DisconnectedState(this);
    }

    public void setState(@NonNull State oldState, @NonNull  State newState) {
        if(this.state != oldState) {
            return;
        }

        SHNLogger.i(TAG, String.format("State changed (%s -> %s)", oldState.getClass().getSimpleName(), newState.getClass().getSimpleName()));

        this.state.breakdown();
        this.state = newState;
        this.state.setup();

        this.sharedResources.notifyStateToListener();
    }

    public State getState() {
        return state;
    }

    public SharedResources getSharedResources() {
        return sharedResources;
    }
}
