/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

public class BleUUIDCreator {
    private static final String BLUETOOTH_BASE_UUID_PREFIX = "00000000";
    private static final String BLUETOOTH_BASE_UUID_SUFFIX = "-0000-1000-8000-00805F9B34FB";
    public static final String BLUETOOTH_BASE_UUID = BLUETOOTH_BASE_UUID_PREFIX + BLUETOOTH_BASE_UUID_SUFFIX;


    // Bluetooth scanRecord DataTypes
    public static final int TYPE_FLAGS                          = 0x01;
    public static final int TYPE_UUID16_INC                     = 0x02;
    public static final int TYPE_UUID16                         = 0x03;
    public static final int TYPE_UUID32_INC                     = 0x04;
    public static final int TYPE_UUID32                         = 0x05;
    public static final int TYPE_UUID128_INC                    = 0x06;
    public static final int TYPE_UUID128                        = 0x07;
    public static final int TYPE_LOCAL_NAME_SHORT               = 0x08;
    public static final int TYPE_LOCAL_NAME_COMPLETE            = 0x09;
    public static final int TYPE_TX_POWER_LEVEL                 = 0x0A;
    public static final int TYPE_DEVICE_CLASS                   = 0x0D;
    public static final int TYPE_SIMPLE_PAIRING_HASH_C          = 0x0E;
    public static final int TYPE_SIMPLE_PAIRING_RANDOMIZER_R    = 0x0F;
    public static final int TYPE_TK_VALUE                       = 0x10;
    public static final int TYPE_SECURITY_MANAGER_OOB_FLAGS     = 0x11;
    public static final int TYPE_CONNECTION_INTERVAL_RANGE      = 0x12;
    public static final int TYPE_SERVICE_UUIDS_LIST_16BIT       = 0x14;
    public static final int TYPE_SERVICE_UUIDS_LIST_128BIT      = 0x15;
    public static final int TYPE_SERVICE_DATA                   = 0x16;
    public static final int TYPE_MANUFACTURER_SPECIFIC_DATA     = 0xFF;


    public static String create128bitBleUUIDFrom16BitBleUUID(long ble16BitUUID) {
        return String.format("%1$08X", ble16BitUUID)+BLUETOOTH_BASE_UUID_SUFFIX;
    }

    
}
