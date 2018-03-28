package com.philips.pins.shinelib.statemachine;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;

import java.util.UUID;

public abstract class State implements BTGatt.BTGattCallback {
    protected StateContext context;

    public State(StateContext context) {
        this.context = context;
    }

    void connect() {
        //Empty implementation
    }

    void connect(long connectTimeOut) {
        //Empty implementation
    }

    void connect(final boolean withTimeout, final long timeoutInMS) {
        //Empty implementation
    }

    void disconnect() {
        //Empty implementation
    }

    void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        //Empty implementation
    }

    void onCharacteristicDiscovered(@NonNull final UUID characteristicUuid, final byte[] data, @Nullable final SHNCharacteristic characteristic) {
        //Empty implementation
    }

    void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState) {
        //Empty implementation
    }

    void onStateUpdated(@NonNull SHNCentral shnCentral) {
        //Empty implementation
    }

    @Override
    public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
        //Empty implementation
    }

    @Override
    public void onServicesDiscovered(BTGatt gatt, int status) {
        //Empty implementation
    }

    @Override
    public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
        //Empty implementation
    }

    @Override
    public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        //Empty implementation
    }

    @Override
    public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
        //Empty implementation
    }

    @Override
    public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        //Empty implementation
    }

    @Override
    public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        //Empty implementation
    }

    @Override
    public void onReliableWriteCompleted(BTGatt gatt, int status) {
        //Empty implementation
    }

    @Override
    public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
        //Empty implementation
    }

    @Override
    public void onMtuChanged(BTGatt gatt, int mtu, int status) {
        //Empty implementation
    }
}