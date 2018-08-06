/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

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
}
