/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.servertime;

import android.support.multidex.MultiDex;

import com.philips.ntputils.ServerTime;
import com.philips.ntputils.constants.ServerTimeConstants;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class ServerTimeTest {

    @Before
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    @Test
    public void testRefreshOffset() {
        ServerTime.getCurrentTime();
        ServerTime.refreshOffset();
        assertNull(ServerTime.getCurrentUTCTimeWithFormat("dd-mm-yyyy"));
    }

    @Test
    public void testRefreshOffsetCall() {
        final SimpleDateFormat sdf = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT, Locale.ROOT);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
        String firstJan1970 = sdf.format(date);

        assertNotSame(firstJan1970, ServerTime.getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT));
    }
}