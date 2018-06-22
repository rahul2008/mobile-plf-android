/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Shadow for {@link android.bluetooth.BluetoothAdapter}.
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(BluetoothAdapter.class)
public class ShadowBluetoothLEAdapter {
    private static final int ADDRESS_LENGTH = 17;

    private Set<BluetoothDevice> bondedDevices = new HashSet<BluetoothDevice>();
    private Set<BluetoothAdapter.LeScanCallback> leScanCallbacks = new HashSet<BluetoothAdapter.LeScanCallback>();
    private boolean isDiscovering;
    private String address;
    private boolean enabled;
    private boolean isOffloadedScanBatchingSupported;
    private int state;
    private BluetoothLeScanner scanner;
    private Map<String, BluetoothDevice> devices;

    @Implementation
    public static BluetoothAdapter getDefaultAdapter() {
        return (BluetoothAdapter) ShadowApplication.getInstance().getBluetoothAdapter();
    }

    @Implementation
    public BluetoothDevice getRemoteDevice(String address) {
        return devices.get(address);
    }

    public void setRemoteDevice(String address, BluetoothDevice device) {
        if (devices == null) {
            devices = new HashMap<>();
        }
        devices.put(address, device);
    }

    @Implementation
    public Set<BluetoothDevice> getBondedDevices() {
        return Collections.unmodifiableSet(bondedDevices);
    }

    public void setBondedDevices(Set<BluetoothDevice> bluetoothDevices) {
        bondedDevices = bluetoothDevices;
    }

    @Implementation
    public boolean startDiscovery() {
        isDiscovering = true;
        return true;
    }

    @Implementation
    public boolean cancelDiscovery() {
        isDiscovering = false;
        return true;
    }

    public void setBluetoothLeScanner(BluetoothLeScanner scanner) {
        this.scanner = scanner;
    }

    @Implementation
    public BluetoothLeScanner getBluetoothLeScanner() {
        return scanner;
    }

    @Implementation
    public boolean startLeScan(BluetoothAdapter.LeScanCallback callback) {
        return startLeScan(null, callback);
    }

    @Implementation
    public boolean startLeScan(UUID[] serviceUuids, BluetoothAdapter.LeScanCallback callback) {
        // Ignoring the serviceUuids param for now.
        leScanCallbacks.add(callback);
        return true;
    }

    @Implementation
    public void stopLeScan(BluetoothAdapter.LeScanCallback callback) {
        leScanCallbacks.remove(callback);
    }

    public boolean isScanning() {
        return leScanCallbacks.size() > 0;
    }

    public Set<BluetoothAdapter.LeScanCallback> getLeScanCallbacks() {
        return Collections.unmodifiableSet(leScanCallbacks);
    }

    public BluetoothAdapter.LeScanCallback getSingleLeScanCallback() {
        if (leScanCallbacks.size() != 1) {
            throw new IllegalStateException("There are " + leScanCallbacks.size() + " callbacks");
        }
        return leScanCallbacks.iterator().next();
    }

    @Implementation
    public boolean isDiscovering() {
        return isDiscovering;
    }

    @Implementation
    public boolean isEnabled() {
        return enabled;
    }

    @Implementation
    public boolean enable() {
        enabled = true;
        return true;
    }

    @Implementation
    public boolean disable() {
        enabled = false;
        return true;
    }

    @Implementation
    public String getAddress() {
        return this.address;
    }

    @Implementation
    public int getState() {
        return state;
    }

    /**
     * Validate a Bluetooth address, such as "00:43:A8:23:10:F0"
     * <p>Alphabetic characters must be uppercase to be valid.
     *
     * @param address Bluetooth address as string
     * @return true if the address is valid, false otherwise
     */
    @Implementation
    public static boolean checkBluetoothAddress(String address) {
        if (address == null || address.length() != ADDRESS_LENGTH) {
            return false;
        }
        for (int i = 0; i < ADDRESS_LENGTH; i++) {
            char c = address.charAt(i);
            switch (i % 3) {
                case 0:
                case 1:
                    if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F')) {
                        // hex character, OK
                        break;
                    }
                    return false;
                case 2:
                    if (c == ':') {
                        break;  // OK
                    }
                    return false;
            }
        }
        return true;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setIsOffloadedScanBatchingSupported(boolean isOffloadedScanBatchingSupported) {
        this.isOffloadedScanBatchingSupported = isOffloadedScanBatchingSupported;
    }

    @Implementation
    public boolean isOffloadedScanBatchingSupported() {
        return isOffloadedScanBatchingSupported;
    }
}
