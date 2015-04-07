package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

/**
 * Created by 310188215 on 26/03/15.
 */
public class SHNCharacteristic {
    private static final String TAG = SHNCharacteristic.class.getSimpleName();

    public enum State {Inactive, Active}

    private final UUID uuid;
    private final SHNDevice shnDevice;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private State state;

    public SHNCharacteristic(SHNDevice shnDevice, UUID characteristicUUID) {
        this.uuid = characteristicUUID;
        this.shnDevice = shnDevice;
        this.state = State.Inactive;
    }

    public State getState() {
        return state;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void connectToBLELayer(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        state = State.Active;
    }

    public void disconnectFromBLELayer() {
        bluetoothGattCharacteristic = null;
        state = State.Inactive;
    }

    public byte[] getValue() {
        if (bluetoothGattCharacteristic != null) {
            return bluetoothGattCharacteristic.getValue();
        }
        return null;
    }

    public boolean readCharacteristic(SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            return shnDevice.readCharacteristic(bluetoothGattCharacteristic, resultReporter);
        } else {
            return false;
        }
    }

    public boolean setCharacteristicNotification(boolean enable, SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            return shnDevice.setCharacteristicNotification(bluetoothGattCharacteristic, enable, resultReporter);
        } else {
            return false;
        }
    }

}
