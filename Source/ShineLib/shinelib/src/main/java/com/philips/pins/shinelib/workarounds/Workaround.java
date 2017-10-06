/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

import android.os.Build;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;

/**
 * Contains all the workarounds that are inside BlueLib.
 *
 * A workaround is, or is not, executed based on the phone's model, manufacturer or OS version.
 * Each workaround specifies on which model, manufacturer and OS version it should execute.
 *
 * If the executeWorkaroundIfPhoneNotRegistered is set true the workaround is also executed
 * on phones the are not known in the {@link Phone} enum.
 */
public enum Workaround {
    /**
     * Clears the BLE cache, fixes Android 7.0 bond suddenly gone problem.
     *
     * After a lot of BLE connections with a device the internal Android BLE cache becomes corrupt.
     * The cache contains temp information about the bond and services.
     * The only solution to restore a corrupted cache is to reboot the phone or restart bluetooth.
     * This fix clears the cache each time it connects with a device, this way it wont become corrupted.
     */
    CorruptedCache(
            new Phone[]         { },
            new Manufacturer[]  { },
            new OS[]            { OS.Nougat },
            false
    ),
    /**
     * Wait some time before executing a BLE read/write action after service discovery is completed.
     * Otherwise the action is stated as successfully started but no callback is ever received from the OS.
     * This causes BlueLib to wait infinitely and the connect timeout will occur.
     */
    ServiceDiscoveredDelay(
            new Phone[]         { Phone.Samsung_S7, Phone.Samsung_S4, Phone.Nexus_6P },
            new Manufacturer[]  { },
            new OS[]            { },
            false
    );

    private static final String TAG = BTGatt.class.getSimpleName();
    private static final boolean ENABLE_DEBUG_LOGGING = false;

    private Phone[] phones;
    private Manufacturer[] manufacturers;
    private OS[] osVersions;
    private boolean executeWorkaroundIfPhoneNotRegistered;

    Workaround(Phone[] phones, Manufacturer[] manufacturers, OS[] osVersions, boolean executeWorkaroundIfPhoneNotRegistered) {
        this.phones = phones;
        this.manufacturers = manufacturers;
        this.osVersions = osVersions;
        this.executeWorkaroundIfPhoneNotRegistered = executeWorkaroundIfPhoneNotRegistered;
    }

    public boolean isRequiredOnThisDevice() {
        String manufacturerName = Build.MANUFACTURER;
        String modelName = Build.MODEL;
        int osVersion = Build.VERSION.SDK_INT;

        DebugLog(String.format("manufacturer: %s, model: %s, os: %d", manufacturerName, modelName, osVersion));

        if(executeWorkaroundIfPhoneNotRegistered && !Phone.isRegistered(modelName, manufacturerName)) {
            DebugLog("Workaround is applied because the phone is not registered.");
            return true;
        }

        for(Phone phone : phones) {
            if (phone.getModelName().equalsIgnoreCase(modelName) &&
                    phone.getManufacturer().getName().equalsIgnoreCase(manufacturerName)) {
                DebugLog("Workaround is applied because of the phone model");
                return true;
            }
        }

        for(Manufacturer manufacturer : manufacturers) {
            if(manufacturer.getName().equalsIgnoreCase(manufacturerName)) {
                DebugLog("Workaround is applied because of the phone manufacturer");
                return true;
            }
        }

        for(OS os : osVersions) {
            for(int version : os.geVersions()) {
                if(version == osVersion) {
                    DebugLog("Workaround is applied because of the phone OS version");
                    return true;
                }
            }
        }

        DebugLog("Workaround not required on this phone.");

        return false;
    }

    private void DebugLog(String log) {
        if (ENABLE_DEBUG_LOGGING) {
            SHNLogger.i(TAG, log);
        }
    }
}
