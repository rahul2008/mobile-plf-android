package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;

import java.util.UUID;

public abstract class State {
    protected StateContext context;

    public State(StateContext context) {
        this.context = context;
    }

    public abstract SHNDevice.State getExternalState();

    public void connect() {
        //Empty implementation
    }

    public void connect(long connectTimeOut) {
        //Empty implementation
    }

    public void connect(final boolean withTimeout, final long timeoutInMS) {
        //Empty implementation
    }

    public void disconnect() {
        //Empty implementation
    }

    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        //Empty implementation
    }

    public void onCharacteristicDiscovered(@NonNull final UUID characteristicUuid, final byte[] data, @Nullable final SHNCharacteristic characteristic) {
        //Empty implementation
    }

    public void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        //Empty implementation
    }

    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        //Empty implementation
    }

    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        //Empty implementation
    }

    public void onServicesDiscovered(BTGatt gatt, int status) {
        //Empty implementation
    }
}