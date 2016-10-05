/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.servertime;

import android.test.InstrumentationTestCase;

import com.philips.ntputils.ServerTime;
import com.philips.ntputils.constants.ServerTimeConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.timesync.TimeInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class ServerTimeTest extends InstrumentationTestCase {
    TimeInterface timeInterface;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }


    public void testRefreshOffset(){
        timeInterface = new AppInfra(getInstrumentation().getContext()).getTime();
        ServerTime.init(timeInterface);
        ServerTime.getCurrentTime();
        ServerTime.refreshOffset();
        assertNull(ServerTime.getCurrentUTCTimeWithFormat("dd-mm-yyyy"));
    }



    public void testRefreshOffsetCall(){
        final SimpleDateFormat sdf = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT, Locale.ROOT);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
        String firstJan1970 = sdf.format(date);

        assertNotSame(firstJan1970, ServerTime.getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT));
    }

}