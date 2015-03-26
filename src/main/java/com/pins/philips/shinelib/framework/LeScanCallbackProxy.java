package com.pins.philips.shinelib.framework;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.pins.philips.shinelib.bletestsupport.BleUtilities;

import java.util.UUID;

/**
 * Created by 310188215 on 11/03/15.
 */
/* This class prevents the Android Bluetooth stack from holding on the object that implements the
 * LeScanCallback and *everything it references* after stopping the scan.
 *
 * As an extra the class is used to provide a general purpose parameter in the device detected callback.
 */
public class LeScanCallbackProxy implements BluetoothAdapter.LeScanCallback {
    public interface LeScanCallback {
        void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    }
    private LeScanCallback leScanCallback;

    public boolean startLeScan(LeScanCallback leScanCallback, Object callbackParameter) {
        this.leScanCallback = leScanCallback;
        return BleUtilities.startLeScan(this);
    }

    public boolean startLeScan(UUID[] serviceUuids, LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
        return BleUtilities.startLeScan(serviceUuids, this);
    }

    public void stopLeScan(LeScanCallback leScanCallback) {
        if (leScanCallback == this.leScanCallback) {
            BleUtilities.stopLeScan(this);
            this.leScanCallback = null;
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (leScanCallback != null) {
            leScanCallback.onLeScan(device, rssi, scanRecord);
        }
    }

}

