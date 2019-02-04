/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import com.philips.platform.appinfra.timesync.TimeInterface;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Server time class which migrate to ntp time
 */
public class ServerTime {
    public static final String OFFSET = "offset";
    public static final String OFFSET_ELAPSED = "offsetElapsed";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_COPPA = "yyyy-MM-dd HH:mm:ss Z";
    public static final String DATE_FORMAT_FOR_JUMP = "yyyy-MM-dd HH:mm:ss";
    public static final String UTC = "UTC";

    public static final int POOL_SIZE = 4;
    private static TimeInterface mTimeInterface;

    public static void init(final TimeInterface timeInterface) {
        mTimeInterface = timeInterface;
    }

    public static String getCurrentTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat(
                DATE_FORMAT, Locale.ROOT);
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTime.UTC));
        if (mTimeInterface != null) {
            return sdf.format(mTimeInterface.getUTCTime());
        }
        return null;
    }


    public static String getCurrentUTCTimeWithFormat(final String pFormat) {
        synchronized (ServerTime.class) {
            final SimpleDateFormat sdf = new SimpleDateFormat(pFormat, Locale.ROOT);
            sdf.setTimeZone(TimeZone.getTimeZone(ServerTime.UTC));
            if (mTimeInterface != null) {
                return sdf.format(mTimeInterface.getUTCTime());
            }
            return null;
        }
    }

    public static synchronized void refreshOffset() {
        synchronized (ServerTime.class) {
            if (mTimeInterface != null) {
                mTimeInterface.refreshTime();
            }
        }
    }
}
