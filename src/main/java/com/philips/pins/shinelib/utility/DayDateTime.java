package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by 310188215 on 07/07/15.
 */
public class DayDateTime {
    public final Date date;
    public final DayOfWeek dayOfWeek;

    public DayDateTime(ByteBuffer byteBuffer) {
        date = SHNBluetoothDataConverter.getDateTime(byteBuffer);
        switch(byteBuffer.get()) {
            case 1:
                dayOfWeek = DayOfWeek.Monday;
                break;
            case 2:
                dayOfWeek = DayOfWeek.Tuesday;
                break;
            case 3:
                dayOfWeek = DayOfWeek.Wednesday;
                break;
            case 4:
                dayOfWeek = DayOfWeek.Thursday;
                break;
            case 5:
                dayOfWeek = DayOfWeek.Friday;
                break;
            case 6:
                dayOfWeek = DayOfWeek.Saturday;
                break;
            case 7:
                dayOfWeek = DayOfWeek.Sunday;
                break;
            default:
                dayOfWeek = DayOfWeek.Unknown;
                break;
        }
    }
}
