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

public class DisconnectingState extends State {

    private static final long DISCONNECT_TIMEOUT = 10_000L;

    private Timer disconnectTimer = Timer.createTimer(new Runnable() {
        @Override
        public void run() {
            handleGattDisconnectEvent();
        }
    }, DISCONNECT_TIMEOUT);

    public DisconnectingState(StateContext context) {
        super(context);

        disconnectTimer.restart();

        BTGatt btGatt = context.getBtGatt();
        if(btGatt != null) {
            SHNLogger.e("DisconnectingState", "Called disconnect");
            btGatt.disconnect();
        } else {
            handleGattDisconnectEvent();
        }
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
            BTGatt btGatt = context.getBtGatt();
            if(btGatt != null) {
                btGatt.disconnect();
            }
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            handleGattDisconnectEvent();
        }
    }

    private void handleGattDisconnectEvent() {
        SHNLogger.e("DisconnectingState", "handleGattDisconnectEvent");

        disconnectTimer.stop();

        BTGatt btGatt = context.getBtGatt();
        if(btGatt != null) {
            btGatt.close();
        }
        context.setBtGatt(null);

        for (SHNService shnService : context.getSHNServices().values()) {
            shnService.disconnectFromBLELayer();
        }

        context.setState(new DisconnectedState(context));
    }
}
