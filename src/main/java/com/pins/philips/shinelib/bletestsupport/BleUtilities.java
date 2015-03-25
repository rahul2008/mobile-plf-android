package com.pins.philips.shinelib.bletestsupport;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.UUID;

/**
 * Created by 310188215 on 06/03/15.
 */
public class BleUtilities {
    private static final String TAG = BleUtilities.class.getSimpleName();
    private static BleUtilities instance;
    private Context applicationContext;

    public static void init(Context applicationContext) {
        Log.e(TAG, "init");
        if (instance == null) {
            instance = new BleUtilities();
        }
        instance.applicationContext = applicationContext;
    }

    public static void setInstance(BleUtilities bleUtilitiesInstance) {
        Log.e(TAG, "setInstance");
        instance = bleUtilitiesInstance;
    }


    public static boolean deviceHasBle() {
        return instance._deviceHasBle();
    }
    public boolean _deviceHasBle() {
        return applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static boolean isBluetoothAdapterEnabled() {
        return instance._isBluetoothAdapterEnabled();
    }
    public boolean _isBluetoothAdapterEnabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter().isEnabled();
    }

    public static void startEnableBluetoothActivity() {
        instance._startEnableBluetoothActivity();
    }
    public void _startEnableBluetoothActivity() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    public static boolean startLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        Log.e(TAG, "startLeScan: " + leScanCallback);
        return instance._startLeScan(leScanCallback);
    }
    public boolean _startLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        Log.e(TAG, "_startLeScan: " + leScanCallback);
        BluetoothManager bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        return bluetoothAdapter.startLeScan(leScanCallback);
    }

    public static boolean startLeScan(UUID[] serviceUUIDs, BluetoothAdapter.LeScanCallback leScanCallback) {
        Log.e(TAG, "startLeScan: " + leScanCallback);
        return instance._startLeScan(serviceUUIDs, leScanCallback);
    }
    public boolean _startLeScan(UUID[] serviceUUIDs, BluetoothAdapter.LeScanCallback leScanCallback) {
        Log.e(TAG, "_startLeScan: " + leScanCallback);
        BluetoothManager bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        return bluetoothAdapter.startLeScan(serviceUUIDs, leScanCallback);
    }

    public static void stopLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        Log.e(TAG, "stopLeScan: " + leScanCallback);
        instance._stopLeScan(leScanCallback);
    }
    public void _stopLeScan(BluetoothAdapter.LeScanCallback leScanCallback) {
        Log.e(TAG, "_stopLeScan: " + leScanCallback);
        BluetoothManager bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    public static BluetoothDevice getRemoteDevice(String macAddress) {
        return instance._getRemoteDevice(macAddress);
    }
    public BluetoothDevice _getRemoteDevice(String macAddress) {
        BluetoothManager bluetoothManager = (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        return bluetoothAdapter.getRemoteDevice(macAddress);
    }
}
