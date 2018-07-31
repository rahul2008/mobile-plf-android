package com.philips.platform.appinfra.utility;

import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AIUtility {

    /**
     * Converts string to date
     *
     * @param strDate string that has to be converted to date
     * @return Date Converted date
     */
    public static Date convertStringToDate(String strDate) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSZ").getParser())
                .appendOptional(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss Z").getParser())
                .appendOptional(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z").getParser())
                .toFormatter();
        return formatter.parseDateTime(strDate).toDate();
    }

    /**
     * Converts date to string
     *
     * @param date date that has to be converted to string
     * @return String Converted string
     */
    public static String convertDateToString(Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
        return String.valueOf(dateFormat.format(date));
    }
}
