package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

public class ReadyState extends SHNDeviceState {

    private static final String TAG = ReadyState.class.getSimpleName();

    public ReadyState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {

    }

    @Override
    protected void onExit() {

    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connected;
    }

    @Override
    public void connect() {
        sharedResources.notifyStateToListener();
    }

    @Override
    public void connect(long connectTimeOut) {
        sharedResources.notifyStateToListener();
    }

    @Override
    public void connect(final boolean withTimeout, final long timeoutInMS) {
        sharedResources.notifyStateToListener();
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");

        if (state == SHNService.State.Error) {
            stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
        }
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
        if(btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);
        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state ReadyState");
        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }
}
