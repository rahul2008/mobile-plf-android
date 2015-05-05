package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import com.pins.philips.shinelib.bluetoothwrapper.BTGatt;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 310188215 on 26/03/15.
 */
public class SHNCharacteristic {
    private static final String TAG = SHNCharacteristic.class.getSimpleName();
    private static final boolean LOGGING = false;
    private static final String CLIENT_CHARACTERISTIC_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    public interface SHNCharacteristicChangedListener {
        void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data);
    }

    public enum State {Inactive, Active}

    private final UUID uuid;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private BTGatt btGatt;
    private State state;
    private SHNCharacteristicChangedListener shnCharacteristicChangedListener;
    private List<SHNDevice.SHNGattCommandResultReporter> pendingCompletions;

    public SHNCharacteristic(UUID characteristicUUID) {
        this.uuid = characteristicUUID;
        this.state = State.Inactive;
        this.pendingCompletions = new LinkedList<>();
    }

    public State getState() {
        return state;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void connectToBLELayer(BTGatt btGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.btGatt = btGatt;
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        state = State.Active;
    }

    public void disconnectFromBLELayer() {
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

    public boolean write(byte[] data, SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            btGatt.writeCharacteristic(bluetoothGattCharacteristic, data);
            pendingCompletions.add(resultReporter);
        } else {
            if (LOGGING) Log.i(TAG, "Error write; characteristic not active: " + uuid);
            return false;
        }
        return true;
    }

    public boolean read(SHNDevice.SHNGattCommandResultReporter resultReporter) {
        if (state == State.Active) {
            btGatt.readCharacteristic(bluetoothGattCharacteristic);
            pendingCompletions.add(resultReporter);
        } else {
            if (LOGGING) Log.i(TAG, "Error read; characteristic not active: " + uuid);
            return false;
        }
        return true;
    }

    public boolean setNotification(boolean enable, SHNDevice.SHNGattCommandResultReporter resultReporter) {
        boolean ret = false;
        if (state == State.Active) {
            if (btGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable)) {
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                btGatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                pendingCompletions.add(resultReporter);
                ret = true;
            }
        }
        return ret;
    }

    public void setShnCharacteristicChangedListener(SHNCharacteristicChangedListener shnCharacteristicChangedListener) {
        this.shnCharacteristicChangedListener = shnCharacteristicChangedListener;
    }

    public void onReadWithData(BTGatt gatt, int status, byte[] data) {
        if (LOGGING) Log.i(TAG, "onReadWithData " + getUuid() + " size = " + pendingCompletions.size());
        SHNDevice.SHNGattCommandResultReporter completion = pendingCompletions.remove(0);
        if (completion != null) completion.reportResult(SHNResult.SHNOk); // TODO: perhaps use data, use status
    }

    public void onWrite(BTGatt gatt, int status) {
        if (LOGGING) Log.i(TAG, "onWrite " + getUuid() + " size = " + pendingCompletions.size());
        SHNDevice.SHNGattCommandResultReporter completion = pendingCompletions.remove(0);
        if (completion != null) completion.reportResult(SHNResult.SHNOk); // TODO; use status
    }

    public void onChanged(BTGatt gatt, byte[] data) {
        if (LOGGING) Log.i(TAG, "onCharacteristicChanged");
        if (shnCharacteristicChangedListener != null) {
            shnCharacteristicChangedListener.onCharacteristicChanged(this, data);
        }
    }

    public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        //throw new UnsupportedOperationException("onDescriptorReadWithData");
        if (LOGGING) Log.i(TAG, "onDescriptorReadWithData " + descriptor.getUuid() + " for characteristic " + getUuid());
    }

    public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (LOGGING) Log.i(TAG, "onDescriptorWrite " + getUuid() + " size = " + pendingCompletions.size());
        SHNDevice.SHNGattCommandResultReporter completion = pendingCompletions.remove(0);
        if (completion != null) completion.reportResult(SHNResult.SHNOk); // TODO; use status
    }
}
