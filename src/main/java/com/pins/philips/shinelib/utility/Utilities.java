package com.pins.philips.shinelib.utility;

/**
 * Created by 310188215 on 15/04/15.
 */
public class Utilities {
    public static String byteToString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append(data.length).append(" ");
        for (byte value: data) {
            sb.append(String.format("0x%02X ", value));
        }
        return sb.toString();
    }
}
