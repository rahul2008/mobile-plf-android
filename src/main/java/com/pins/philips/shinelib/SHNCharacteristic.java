package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.UUID;

/**
 * Created by 310188215 on 26/03/15.
 */
public class SHNCharacteristic {
    private static final String TAG = SHNCharacteristic.class.getSimpleName();
    private static final boolean LOGGING = false;

    public interface SHNCharacteristicChangedListener {
        void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data);
    }

    public enum State {Inactive, Active}

    private final UUID uuid;
    private final SHNDevice shnDevice;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private State state;
    private SHNCharacteristicChangedListener shnCharacteristicChangedListener;

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

    public boolean writeCharacteristic(byte[] data, SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            int properties = bluetoothGattCharacteristic.getProperties();
            if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0) {
                if (LOGGING) Log.i(TAG, "Success writeCharacteristic: " + uuid);
                return shnDevice.writeCharacteristic(data, bluetoothGattCharacteristic, resultReporter);
            }
            if (LOGGING) Log.i(TAG, "Error writeCharacteristic property cannot be written: " + uuid);
            return false;
        } else {
            if (LOGGING) Log.i(TAG, "Error writeCharacteristic characteristic not active: " + uuid);
            return false;
        }
    }

    public boolean readCharacteristic(SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            int properties = bluetoothGattCharacteristic.getProperties();
            if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
                if (LOGGING) Log.i(TAG, "Success readCharacteristic: " + uuid);
                return shnDevice.readCharacteristic(bluetoothGattCharacteristic, resultReporter);
            }
            if (LOGGING) Log.i(TAG, "Error readCharacteristic property cannot be read: " + uuid);
            return false;
        } else {
            if (LOGGING) Log.i(TAG, "Error readCharacteristic characteristic not active: " + uuid);
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

    public void setShnCharacteristicChangedListener(SHNCharacteristicChangedListener shnCharacteristicChangedListener) {
        this.shnCharacteristicChangedListener = shnCharacteristicChangedListener;
    }

    public void onCharacteristicChanged(byte[] data) {
        if (LOGGING) Log.i(TAG, "onCharacteristicChanged");
        if (shnCharacteristicChangedListener != null) {
            shnCharacteristicChangedListener.onCharacteristicChanged(this, data);
        }
    }
}
