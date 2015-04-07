package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by 310188215 on 26/03/15.
 */
public class SHNCharacteristic {
    private static final String TAG = SHNCharacteristic.class.getSimpleName();

    public enum State {Inactive, Active};
    private final UUID uuid;
    private final SHNDevice shnDevice;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private State state;

    public SHNCharacteristic(SHNDevice shnDevice, UUID characteristicUUID) {
        this.uuid = characteristicUUID;
        this.shnDevice = shnDevice;
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

    public void readIt(SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            shnDevice.readIt(bluetoothGattCharacteristic, resultReporter);
        } else {
            // TODO Report error by returning false (command not queued)
        }
    }

}
