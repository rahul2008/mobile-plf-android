package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;

public abstract class SHNConnectingState extends SHNDeviceState {

    private static final String TAG = "SHNConnectingState";

    private Timer connectingTimer;

    public SHNConnectingState(@NonNull final SHNDeviceStateMachine stateMachine, long connectTimeOut) {
        super(stateMachine);

        if (connectTimeOut > 0) {
            connectingTimer = Timer.createTimer(new Runnable() {
                @Override
                public void run() {
                    SHNLogger.e(TAG, "connect timeout in SHNConnectingState");
                    stateMachine.getSharedResources().notifyFailureToListener(SHNResult.SHNErrorTimeout);
                    stateMachine.setState(new SHNDisconnectingState(stateMachine));
                }
            }, connectTimeOut);
        }
    }

    @Override
    protected void onEnter() {
        if (connectingTimer != null) connectingTimer.restart();
    }

    @Override
    protected void onExit() {
        if (connectingTimer != null) connectingTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state SHNConnectingState");
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
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
        BTGatt btGatt = stateMachine.getSharedResources().getBtGatt();
        if (btGatt != null) {
            btGatt.close();
        }
        stateMachine.getSharedResources().setBtGatt(null);

        stateMachine.getSharedResources().notifyFailureToListener(SHNResult.SHNErrorInvalidState);

        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }
}
