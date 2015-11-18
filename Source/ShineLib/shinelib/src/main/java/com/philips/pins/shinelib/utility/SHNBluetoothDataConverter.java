/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNBluetoothDataConverter {

    public static Date getDate(ByteBuffer byteBuffer) {
        // get values from byteBuffer
        int year = ScalarConverters.ushortToInt(byteBuffer.getShort());
        int month = ScalarConverters.ubyteToInt(byteBuffer.get());
        int day = ScalarConverters.ubyteToInt(byteBuffer.get());

        if (year < 1582 || year > 9999) {
            throw new IllegalArgumentException();
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException();
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException();
        }

        // Construct date
        Date date = new Date(year - 1900, month - 1, day);
        return date;
    }

    public static Date getDateTime(ByteBuffer byteBuffer) {
        // get values from byteBuffer
        Date date = getDate(byteBuffer);
        int hours = ScalarConverters.ubyteToInt(byteBuffer.get());
        int minutes = ScalarConverters.ubyteToInt(byteBuffer.get());
        int seconds = ScalarConverters.ubyteToInt(byteBuffer.get());

        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException();
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException();
        }
        if (seconds < 0 || seconds > 59) {
            throw new IllegalArgumentException();
        }

        // Add time to the date
        date.setHours(hours);
        date.setMinutes(minutes);
        date.setSeconds(seconds);
        return date;
    }

    public static byte[] convertDateToByteArray(Date date){
        ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putShort((short) (date.getYear() + 1900));
        byteBuffer.put((byte) (date.getMonth() + 1));
        byteBuffer.put((byte) date.getDate());

        return byteBuffer.array();
    }
}
