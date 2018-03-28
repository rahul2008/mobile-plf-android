package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class DiscoveringServicesState extends SHNDeviceState {

    private static final String TAG = DiscoveringServicesState.class.getSimpleName();

    private static final long DISCOVER_TIMEOUT = 20_000L;

    private Timer discoverTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.e(TAG, "discovery timeout in DiscoveringServicesState");
            sharedResources.notifyFailureToListener(SHNResult.SHNErrorTimeout);
            stateMachine.setState(DiscoveringServicesState.this, new DisconnectingState(stateMachine, sharedResources));
        }
    }, DISCOVER_TIMEOUT);

    public DiscoveringServicesState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {
        discoverTimer.restart();

        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt != null) {
            btGatt.discoverServices();
        }
    }

    @Override
    protected void onExit() {
        discoverTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Connecting;
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        }
    }

    @Override
    public void onServicesDiscovered(BTGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {

            if (gatt.getServices().size() == 0) {
                SHNLogger.i(TAG, "No services found, rediscovery the services");
                gatt.disconnect();
                stateMachine.setState(this, new GattConnectingState(stateMachine, sharedResources));
                return;
            }

            stateMachine.setState(this, new InitializingServicesState(stateMachine, sharedResources));
        } else {
            SHNLogger.e(TAG, "onServicedDiscovered: error discovering services (status = '" + status + "'); disconnecting");
            stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            SHNLogger.e(TAG, "The bluetooth stack didn't disconnect the connection to the peripheral. This is a best effort attempt to solve that.");
            handleGattDisconnectEvent();
        }
    }

    @Override
    public void disconnect() {
        SHNLogger.d(TAG, "Disconnect call in state DiscoveringServicesState");
        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }

    private void handleGattDisconnectEvent() {
        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt != null) {
            btGatt.close();
        }
        sharedResources.setBtGatt(null);

        sharedResources.notifyFailureToListener(SHNResult.SHNErrorInvalidState);

        stateMachine.setState(this, new DisconnectingState(stateMachine, sharedResources));
    }
}
