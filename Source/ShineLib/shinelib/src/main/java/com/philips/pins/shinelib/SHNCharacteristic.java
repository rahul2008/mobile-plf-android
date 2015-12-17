/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;


import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SHNCharacteristic {
    private static final String TAG = SHNCharacteristic.class.getSimpleName();
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private final UUID uuid;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private BTGatt btGatt;
    private State state;
    private SHNCharacteristicChangedListener shnCharacteristicChangedListener;
    private List<SHNCommandResultReporter> pendingCompletions;

    public interface SHNCharacteristicChangedListener {
        void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data);
    }

    public enum State {Inactive, Active}

    public SHNCharacteristic(UUID characteristicUUID) {
        this.uuid = characteristicUUID;
        this.state = State.Inactive;
        this.pendingCompletions = new LinkedList<>();
        SHNLogger.i(TAG, "created: " + uuid);
    }

    public State getState() {
        return state;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void connectToBLELayer(BTGatt btGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        SHNLogger.i(TAG, "connectToBLELayer: " + uuid);
        this.btGatt = btGatt;
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        state = State.Active;
    }

    public void disconnectFromBLELayer() {
        SHNLogger.i(TAG, "disconnectFromBLELayer: " + uuid);
        bluetoothGattCharacteristic = null;
        btGatt = null;
        state = State.Inactive;
    }

    public byte[] getValue() {
        if (bluetoothGattCharacteristic != null) {
            return bluetoothGattCharacteristic.getValue();
        }
        return null;
    }

    public void write(byte[] data, SHNCommandResultReporter resultReporter) {
        if (state == State.Active) {
            btGatt.writeCharacteristic(bluetoothGattCharacteristic, data);
            pendingCompletions.add(resultReporter);
        } else {
            SHNLogger.i(TAG, "Error write; characteristic not active: " + uuid);
            resultReporter.reportResult(SHNResult.SHNErrorInvalidState, null);
        }
    }

    public void read(@NonNull final SHNCommandResultReporter resultReporter) {
        if (state == State.Active) {
            btGatt.readCharacteristic(bluetoothGattCharacteristic);
            pendingCompletions.add(resultReporter);
        } else {
            SHNLogger.i(TAG, "Error read; characteristic not active: " + uuid);
            resultReporter.reportResult(SHNResult.SHNErrorInvalidState, null);
        }
    }

    public boolean setNotification(boolean enable, SHNCommandResultReporter resultReporter) {
        return writeToBtGatt(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, enable, resultReporter);
    }

    public boolean setIndication(boolean enable, SHNCommandResultReporter resultReporter) {
        return writeToBtGatt(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE, enable, resultReporter);
    }

    public void setShnCharacteristicChangedListener(SHNCharacteristicChangedListener shnCharacteristicChangedListener) {
        this.shnCharacteristicChangedListener = shnCharacteristicChangedListener;
    }

    public void onReadWithData(BTGatt gatt, int status, byte[] data) {
        SHNLogger.i(TAG, "onReadWithData");
        SHNResult shnResult = translateGATTResultToSHNResult(status);
        reportResultToCaller(data, shnResult);
    }

    public void onWrite(BTGatt gatt, int status) {
        SHNLogger.i(TAG, "onWrite");
        SHNResult shnResult = translateGATTResultToSHNResult(status);
        reportResultToCaller(null, shnResult);
    }

    public void onChanged(BTGatt gatt, byte[] data) {
        SHNLogger.i(TAG, "onChanged");
        if (shnCharacteristicChangedListener != null) {
            shnCharacteristicChangedListener.onCharacteristicChanged(this, data);
        }
    }

    public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        throw new UnsupportedOperationException("onDescriptorReadWithData");
    }

    public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        SHNLogger.i(TAG, "onDescriptorWrite " + getUuid() + " size = " + pendingCompletions.size());
        SHNResult shnResult = translateGATTResultToSHNResult(status);
        reportResultToCaller(null, shnResult);
    }

    private SHNResult translateGATTResultToSHNResult(int status) {
        SHNResult shnResult = SHNResult.SHNErrorUnknownDeviceType;
        if (status == BluetoothGatt.GATT_SUCCESS) {
            shnResult = SHNResult.SHNOk;
        }
        return shnResult;
    }

    private void reportResultToCaller(byte[] data, SHNResult shnResult) {
        SHNCommandResultReporter completion = pendingCompletions.remove(0);
        if (completion != null) completion.reportResult(shnResult, data);
    }

    private boolean writeToBtGatt(byte[] value, boolean enable, SHNCommandResultReporter resultReporter) {
        if (state == State.Active) {
            if (btGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable)) {
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                if (descriptor == null) {
                    resultReporter.reportResult(SHNResult.SHNErrorUnsupportedOperation, null);
                    return false;
                }
                btGatt.writeDescriptor(descriptor, value);
                pendingCompletions.add(resultReporter);
                return true;
            }
        }
        return false;
    }
}
