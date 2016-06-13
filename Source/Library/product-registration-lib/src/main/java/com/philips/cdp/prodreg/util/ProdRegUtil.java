package com.philips.cdp.prodreg.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegUtil {

    public static boolean isValidDate(final String date) {
        if (date != null) {
            String[] dates = date.split("-");
            return dates.length > 1 && Integer.parseInt(dates[0]) > 1999 && !isFutureDate(date);
        } else return false;
    }

    public static boolean isFutureDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        final String mGetDeviceDate = dateFormat.format(calendar.getTime());
        try {
            final Date mDisplayDate = dateFormat.parse(date);
            final Date mDeviceDate = dateFormat.parse(mGetDeviceDate);
            return mDisplayDate.after(mDeviceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isValidSerialNumber(final String regularExpression, final String serialNumber) {
        return serialNumber == null || serialNumber.length() < 1 || !serialNumber.matches(regularExpression);
    }
}
