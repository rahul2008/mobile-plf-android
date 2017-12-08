/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

public class ScalarConverters {
    public static int ubyteToInt(byte value) {
        int retval = 0;
        retval |= value;
        retval &= 0xFF;
        return retval;
    }

    public static int ushortToInt(short value) {
        int retval = 0;
        retval |= value;
        retval &= 0xFFFF;
        return retval;
    }

    public static long uintToLong(int value) {
        long retval = 0;
        retval |= value;
        retval &= 0xFFFFFFFFl;
        return retval;
    }

    public static byte intToubyte(int value) {
        byte retval = 0;
        retval |= value;
        return retval;
    }

    public static short intToushort(int value) {
        short retval = 0;
        retval |= value;
        return retval;
    }

    public static int longTouint(long value) {
        int retval = 0;
        retval |= value;
        return retval;
    }
}
