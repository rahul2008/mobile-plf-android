package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceState;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.utility.SHNLogger;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;

public class SHNReadyState extends SHNDeviceState {

    private static final String TAG = "SHNReadyState";

    public SHNReadyState(@NonNull SHNDeviceStateMachine stateMachine) {
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
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.d(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");

        if (state == SHNService.State.Error) {
            stateMachine.setState(new SHNDisconnectingState(stateMachine));
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
        if (SHNCentralStateNotReady.equals(shnCentral.getShnCentralState())) {
            SHNLogger.e(TAG, "The bluetooth stack didn't disconnect the connection to the peripheral. This is a best effort attempt to solve that.");
            handleGattDisconnectEvent();
        }
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state SHNReadyState");
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);
        stateMachine.setState(new SHNDisconnectingState(stateMachine));
    }
}
