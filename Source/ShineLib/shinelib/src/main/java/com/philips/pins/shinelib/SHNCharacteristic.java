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
    private static final boolean ENABLE_DEBUG_LOGGING = false;
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
        DebugLog("created: " + uuid);
    }

    public State getState() {
        return state;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void connectToBLELayer(BTGatt btGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        DebugLog("connectToBLELayer: " + uuid);
        this.btGatt = btGatt;
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        state = State.Active;
    }

    public void disconnectFromBLELayer() {
        DebugLog("disconnectFromBLELayer: " + uuid);
        bluetoothGattCharacteristic = null;
        btGatt = null;
        state = State.Inactive;
        while (!pendingCompletions.isEmpty()) {
            reportResultToCaller(null, SHNResult.SHNErrorConnectionLost);
        }
    }

    public byte[] getValue() {
        if (bluetoothGattCharacteristic != null) {
            return bluetoothGattCharacteristic.getValue();
        }
        return null;
    }

    public void write(byte[] data, SHNCommandResultReporter resultReporter) {
        if (state == State.Active) {
            pendingCompletions.add(resultReporter);
            btGatt.writeCharacteristic(bluetoothGattCharacteristic, data);
        } else {
            SHNLogger.w(TAG, "Error write; characteristic not active: " + uuid);
            if (resultReporter != null) {
                resultReporter.reportResult(SHNResult.SHNErrorInvalidState, null);
            }
        }
    }

    public void read(@NonNull final SHNCommandResultReporter resultReporter) {
        if (state == State.Active) {
            pendingCompletions.add(resultReporter);
            btGatt.readCharacteristic(bluetoothGattCharacteristic);
        } else {
            SHNLogger.w(TAG, "Error read; characteristic not active: " + uuid);
            if (resultReporter != null) {
                resultReporter.reportResult(SHNResult.SHNErrorInvalidState, null);
            }
        }
    }

    public void setNotification(boolean enable, @NonNull SHNCommandResultReporter resultReporter) {
        toggleNotificationsOrIndications(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, enable, resultReporter);
    }

    public void setIndication(boolean enable, @NonNull SHNCommandResultReporter resultReporter) {
        toggleNotificationsOrIndications(enable ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, enable, resultReporter);
    }

    public void setShnCharacteristicChangedListener(SHNCharacteristicChangedListener shnCharacteristicChangedListener) {
        this.shnCharacteristicChangedListener = shnCharacteristicChangedListener;
    }

    public void onReadWithData(BTGatt gatt, int status, byte[] data) {
        DebugLog("onReadWithData");
        SHNResult shnResult = translateGATTResultToSHNResult(status);
        reportResultToCaller(data, shnResult);
    }

    public void onWrite(BTGatt gatt, int status) {
        DebugLog("onWrite");
        SHNResult shnResult = translateGATTResultToSHNResult(status);
        reportResultToCaller(null, shnResult);
    }

    public void onChanged(BTGatt gatt, byte[] data) {
        DebugLog("onChanged");
        if (shnCharacteristicChangedListener != null) {
            shnCharacteristicChangedListener.onCharacteristicChanged(this, data);
        }
    }

    public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        throw new UnsupportedOperationException("onDescriptorReadWithData");
    }

    public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        DebugLog("onDescriptorWrite " + getUuid() + " size = " + pendingCompletions.size());
        SHNResult shnResult = translateGATTResultToSHNResult(status);
        reportResultToCaller(null, shnResult);
    }

    private SHNResult translateGATTResultToSHNResult(int status) {
        return status == BluetoothGatt.GATT_SUCCESS ? SHNResult.SHNOk : SHNResult.SHNErrorInvalidResponse;
    }

    private void reportResultToCaller(byte[] data, SHNResult shnResult) {
        SHNCommandResultReporter completion = pendingCompletions.remove(0);
        if (completion != null) completion.reportResult(shnResult, data);
    }

    private void toggleNotificationsOrIndications(byte[] valueToWriteToDescriptor, boolean enable, SHNCommandResultReporter resultReporter) {
        if (state == State.Active) {
            Boolean supported = false;
            if (btGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable)) {
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
                if (descriptor != null) {
                    supported = true;
                    pendingCompletions.add(resultReporter);
                    btGatt.writeDescriptor(descriptor, valueToWriteToDescriptor);
                }
            }

            if (!supported) {
                if (resultReporter != null) {
                    resultReporter.reportResult(SHNResult.SHNErrorUnsupportedOperation, null);
                }
            }
        } else {
            if (resultReporter != null) {
                resultReporter.reportResult(SHNResult.SHNErrorInvalidState, null);
            }
        }
    }

    private void DebugLog(String log) {
        if (ENABLE_DEBUG_LOGGING) {
            DebugLog(log);
        }
    }
}
