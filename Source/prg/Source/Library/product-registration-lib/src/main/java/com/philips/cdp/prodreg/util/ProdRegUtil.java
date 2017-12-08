/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.util;

import android.graphics.*;
import android.text.*;
import android.text.style.*;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.platform.appinfra.timesync.TimeInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProdRegUtil {
    private static final String TAG = ProdRegUtil.class.getSimpleName();

    public boolean isValidDate(final String date) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat(ProdRegConstants.PROD_REG_DATE_FORMAT_SERVER);
            Date currentDate = sd.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            return (cal.get(Calendar.YEAR) > 1999);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("SimpleDateFormat")
    public boolean isFutureDate(String date) {
        try {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ProdRegConstants.PROD_REG_DATE_FORMAT_SERVER);
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
     * @return - Return min date for date picker
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

    /**
     * Return max date for date picker
     *
     * @return - Return max date for date picker
     */
    public long getMaxDate() {
        final TimeInterface serverTime = PRUiHelper.getInstance().getServerTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(serverTime.getUTCTime());
        return calendar.getTimeInMillis();
    }

    public void storeProdRegTaggingMeasuresCount(final ProdRegCache prodRegCache, final String key, final int count) {
        final int intData = prodRegCache.getIntData(key);
        prodRegCache.storeStringData(key, String.valueOf(intData + count));
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

    public String getDisplayDate(String regDate){
        try {
        SimpleDateFormat dt = new SimpleDateFormat(ProdRegConstants.PROD_REG_DATE_FORMAT_SERVER);
        Date date = dt.parse(regDate);
        SimpleDateFormat dt1 = new SimpleDateFormat(ProdRegConstants.PROD_REG_DATE_FORMAT_UI);
            return dt1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return regDate;
        }
    }
    public SpannableString generateSpannableText(String normal, String bold){
        int length= normal.length()+bold.length();
        SpannableString str = new SpannableString(normal +bold);
        str.setSpan(new StyleSpan(Typeface.BOLD), normal.length(),length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }
}
