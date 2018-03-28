package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public abstract class ConnectingState extends SHNDeviceState {

    private static final String TAG = ConnectingState.class.getName();
    private static final long CONNECT_TIMEOUT = 20_000L;

    protected Timer connectingTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.e(TAG, "connect timeout in ConnectingState");
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            stateMachine.setState(ConnectingState.this, new DisconnectingState(stateMachine, sharedResources));
        }
    }, CONNECT_TIMEOUT);

    public ConnectingState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {
        connectingTimer.restart();
    }

    @Override
    protected void onExit() {
        connectingTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state ConnectingState");
        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            SHNLogger.e(TAG, "The bluetooth stack didn't disconnect the connection to the peripheral. This is a best effort attempt to solve that.");
            handleGattDisconnectEvent();
        }
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);

        sharedResources.notifyFailureToListener(SHNResult.SHNErrorInvalidState);

        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }
}
