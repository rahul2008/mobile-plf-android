package com.philips.cdp.prodreg.util;

import android.text.TextUtils;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.localcache.ProdRegCache;

import java.util.Calendar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegUtil {
    public static boolean isValidDate(final String date) {
        return !TextUtils.isEmpty(date);
    }

    public static boolean isValidSerialNumber(final boolean isRequired, final String regularExpression, final String serialNumber) {
        if (isRequired) {
            if (TextUtils.isEmpty(serialNumber)) {
                return false;
            } else if (TextUtils.isEmpty(regularExpression)) {
                return true;
            } else if (!serialNumber.matches(regularExpression)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return min date for date picker
     *
     * @return
     */
    public static long getMinDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(ProdRegConstants.START_DATE_YEAR, ProdRegConstants.START_DATE_MONTH, ProdRegConstants.START_DATE_DAY);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTimeInMillis();
    }

    public static void storeProdRegTaggingMeasuresCount(final ProdRegCache prodRegCache, final String key, final int count) {
        final int intData = prodRegCache.getIntData(key);
        prodRegCache.storeIntData(key, (intData + count));
    }

    public static String getValidatedString(final int value) {
        final String valueString;
        if (value < 10) {
            valueString = "0" + value;
        } else {
            valueString = Integer.toString(value);
        }
        return valueString;
    }
}
