/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.ntputils;

import com.philips.ntputils.constants.ServerTimeConstants;
import com.philips.platform.appinfra.timesync.TimeInterface;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Server time class which migrate to ntp time
 */
public class ServerTime {
    private static TimeInterface mTimeInterface;

    public static void init(final TimeInterface timeInterface) {
        mTimeInterface = timeInterface;
    }

    public static String getCurrentTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat(
                ServerTimeConstants.DATE_FORMAT, Locale.ROOT);
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
        if(mTimeInterface!=null) {
            return sdf.format(mTimeInterface.getUTCTime());
        }
        return  null;
    }


    public static String getCurrentUTCTimeWithFormat(final String pFormat) {
        synchronized (ServerTime.class) {
            final SimpleDateFormat sdf = new SimpleDateFormat(pFormat, Locale.ROOT);
            sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
            if(mTimeInterface!=null) {
                return sdf.format(mTimeInterface.getUTCTime());
            }
           return  null;
        }
    }

    public static synchronized void refreshOffset() {
        synchronized (ServerTime.class) {
            if(mTimeInterface!=null) {
                mTimeInterface.refreshTime();
            }
        }
    }
}
