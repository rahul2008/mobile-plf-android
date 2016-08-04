package com.philips.cdp.prodreg.util;

import android.text.TextUtils;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.logging.ProdRegLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegUtil {
    private static final String TAG = ProdRegUtil.class.getSimpleName();

    public boolean isValidDate(final String date) {
        if (date != null) {
            String[] dates = date.split("-");
            return dates.length > 1 && Integer.parseInt(dates[0]) > 1999 && !isFutureDate(date);
        } else return false;
    }

    public boolean isFutureDate(String date) {
        try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        final String mGetDeviceDate = dateFormat.format(calendar.getTime());
            final Date mDisplayDate = dateFormat.parse(date);
            final Date mDeviceDate = dateFormat.parse(mGetDeviceDate);
            return mDisplayDate.after(mDeviceDate);
        } catch (ParseException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
        return false;
    }

    public boolean isValidSerialNumber(final boolean isRequired, final String regularExpression, final String serialNumber) {
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
    public long getMinDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(ProdRegConstants.START_DATE_YEAR, ProdRegConstants.START_DATE_MONTH, ProdRegConstants.START_DATE_DAY);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTimeInMillis();
    }

    public void storeProdRegTaggingMeasuresCount(final ProdRegCache prodRegCache, final String key, final int count) {
        final int intData = prodRegCache.getIntData(key);
        prodRegCache.storeIntData(key, (intData + count));
    }

    public String getValidatedString(final int value) {
        final String valueString;
        if (value < 10) {
            valueString = "0" + value;
        } else {
            valueString = Integer.toString(value);
        }
        return valueString;
    }
}
