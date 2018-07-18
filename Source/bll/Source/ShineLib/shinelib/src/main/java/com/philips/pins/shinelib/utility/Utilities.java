/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.bluetooth.BluetoothAdapter;

public class Utilities {
    public static String byteToString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            sb.append(data.length).append(" ");
            for (byte value : data) {
                sb.append(String.format("0x%02X ", value));
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    /**
     * Validate a String Bluetooth address, such as "00:43:A8:23:10:F0"
     * <p>Alphabetic characters must be uppercase to be valid.
     *
     * @param address Bluetooth address as string
     * @return true if the address is valid, false otherwise
     */
    public static boolean isValidMacAddress(String address) {
        return BluetoothAdapter.checkBluetoothAddress(address);
    }
}
