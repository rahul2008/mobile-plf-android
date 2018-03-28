package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.SHNLogger;

public class DisconnectingState extends SHNDeviceState {

    private static final String TAG = DisconnectingState.class.getSimpleName();

    private static final long DISCONNECT_TIMEOUT = 1_000L;

    private Timer disconnectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            SHNLogger.e(TAG, "disconnect timeout in DisconnectingState");
            handleGattDisconnectEvent();
        }
    }, DISCONNECT_TIMEOUT);

    public DisconnectingState(StateMachine stateMachine, SharedResources sharedResources) {
        super(stateMachine, sharedResources);
    }

    @Override
    protected void onEnter() {
        disconnectTimer.restart();

        BTGatt btGatt = sharedResources.getBtGatt();
        if(btGatt != null) {
            btGatt.disconnect();
        } else {
            handleGattDisconnectEvent();
        }
    }

    @Override
    protected void onExit() {
        disconnectTimer.stop();
    }

    @Override
    public SHNDevice.State getExternalState() {
        return SHNDevice.State.Disconnecting;
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            BTGatt btGatt = sharedResources.getBtGatt();
            if(btGatt != null) {
                btGatt.disconnect();
            }
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

        for (SHNService shnService : sharedResources.getSHNServices().values()) {
            shnService.disconnectFromBLELayer();
        }

        stateMachine.setState(this, new DisconnectedState(stateMachine, sharedResources));
    }
}
