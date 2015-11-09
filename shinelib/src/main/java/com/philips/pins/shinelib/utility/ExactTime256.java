package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by 310188215 on 07/07/15.
 */
public class ExactTime256 {
    public final DayDateTime dayDateTime;
    public final short fractions256;
    public Date exactTime256Date;

    public ExactTime256(ByteBuffer byteBuffer) {
        dayDateTime = new DayDateTime(byteBuffer);
        fractions256 = (short) ScalarConverters.ubyteToInt(byteBuffer.get());
        exactTime256Date = new Date(dayDateTime.date.getTime() + ((1000 * fractions256) / 256));
    }
}
