package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;

public class DisconnectingState extends State {

    public DisconnectingState(StateContext context) {
        super(context);

        context.getBtGatt().disconnect();
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            handleGattDisconnectEvent();
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            context.getBtGatt().disconnect();
        }
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (shnCentral.getBluetoothAdapterState() == BluetoothAdapter.STATE_OFF) {
            handleGattDisconnectEvent();
        }
    }

    private void handleGattDisconnectEvent() {
        context.getBtGatt().close();
        context.setBtGatt(null);

        for (SHNService shnService : context.getSHNServices().values()) {
            shnService.disconnectFromBLELayer();
        }

        context.setState(new DisconnectedState(context));
    }
}
