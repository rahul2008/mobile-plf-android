/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

import android.os.Build;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Locale;

/**
 * Contains workarounds that are inside BlueLib.
 *
 * A workaround is, or is not, executed based on the phone's model, manufacturer or OS version.
 * Each workaround specifies on which model, manufacturer and OS version it should execute.
 *
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
    CORRUPTED_CACHE(
            new Phone[]         { },
            new Manufacturer[]  { },
            new OS[]            { OS.NOUGAT}
    ),
    /**
     * Extend the minimum connection idle time.
     * This gives the BLE stack more time to prepare for a new connection.
     */
    EXTENDED_MINIMUM_CONNECTION_IDLE_TIME(
            new Phone[]         { Phone.SAMSUNG_S4, Phone.NEXUS_6P},
            new Manufacturer[]  { },
            new OS[]            { }
    );

    private static final String TAG = Workaround.class.getSimpleName();
    private static final boolean ENABLE_DEBUG_LOGGING = false;

    private Phone[] phones;
    private Manufacturer[] manufacturers;
    private OS[] osVersions;

    Workaround(Phone[] phones, Manufacturer[] manufacturers, OS[] osVersions) {
        this.phones = phones;
        this.manufacturers = manufacturers;
        this.osVersions = osVersions;
    }

    public boolean isRequiredOnThisDevice() {
        String manufacturerName = Build.MANUFACTURER;
        String modelName = Build.MODEL;
        int osVersion = Build.VERSION.SDK_INT;

        for(Phone phone : phones) {
            if (phone.getModelName().equalsIgnoreCase(modelName) &&
                    phone.getManufacturer().getName().equalsIgnoreCase(manufacturerName)) {

                DebugLog(String.format("Workaround '%s' is applied because the phone model is %s of %s", name(), modelName, manufacturerName));
                return true;
            }
        }

        for(Manufacturer manufacturer : manufacturers) {
            if(manufacturer.getName().equalsIgnoreCase(manufacturerName)) {
                DebugLog(String.format("Workaround '%s' is applied because the phone manufacturer is %s", name(), manufacturerName));
                return true;
            }
        }

        for(OS os : osVersions) {
            for(int version : os.geVersions()) {
                if(version == osVersion) {
                    DebugLog(String.format(Locale.US, "Workaround '%s' is applied because the phone OS version is %d", name(), osVersion));
                    return true;
                }
            }
        }

        DebugLog(String.format(Locale.US, "Workaround '%s' is not required on this pone. (Manufacturer: %s, Model: %s, OS: %d)", name(), manufacturerName, modelName, osVersion));

        return false;
    }

    private void DebugLog(String log) {
        if (ENABLE_DEBUG_LOGGING) {
            SHNLogger.i(TAG, log);
        }
    }
}
