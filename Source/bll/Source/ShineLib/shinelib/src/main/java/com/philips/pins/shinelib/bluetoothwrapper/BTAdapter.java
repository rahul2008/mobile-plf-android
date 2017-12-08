/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import java.util.UUID;

public class BTAdapter {
    public interface LeScanCallback {
        void onLeScan(BTDevice btDevice, int rssi, byte[] scanRecord);
    }

    private final BluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    private LeScanCallback leScanCallback;

    public BTAdapter(Context context, Handler handler) {
        this.handler = handler;
        Context applicationContext = context.getApplicationContext();
        BluetoothManager bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        // TODO check if the device has Bluetooth and that it is enabled (optionaly enable)
    }

    public BTDevice getRemoteDevice(String address) {
        return new BTDevice(bluetoothAdapter.getRemoteDevice(address), handler);
    }

    public void startLeScan(LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
        bluetoothAdapter.startLeScan(internalLeScanCallback);
    }

    public void startLeScan(UUID[] serviceUuids, LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
        bluetoothAdapter.startLeScan(serviceUuids, internalLeScanCallback);
    }

    public void stopLeScan(LeScanCallback leScanCallback)
    {
        bluetoothAdapter.stopLeScan(internalLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback internalLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            Runnable callback = new Runnable() {
                @Override
                public void run() {
                    leScanCallback.onLeScan(new BTDevice(device, handler), rssi, scanRecord.clone());
                }
            };
            handler.post(callback);
        }
    };
}
