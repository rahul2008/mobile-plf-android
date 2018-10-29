/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.timesync;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * TimeSync Test class.
 */
public class TimeSyncTest {

    private TimeInterface mTimeSyncInterface = null;
    private AppInfra mAppInfra;
    private TimeSyncSntpClient mTimeSyncSntpClient;

    @Before
    protected void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mTimeSyncInterface = mAppInfra.getTime();
        assertNotNull(mTimeSyncInterface);
        mTimeSyncSntpClient = new TimeSyncSntpClient(mAppInfra);
    }

    @Test
    public void testUTCTimeHappyPath() throws Exception {
        assertNotNull(mTimeSyncSntpClient);
        assertNotNull(mTimeSyncInterface.getUTCTime());
    }

    @Test
    public void testRefreshOffsetCall() {
        assertNotNull(mTimeSyncSntpClient);
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeSyncSntpClient.DATE_FORMAT, Locale.ENGLISH);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
    }

    @Test
    public void testgetUTCTime() {
        mTimeSyncSntpClient = new TimeSyncSntpClient(mAppInfra);
        assertNotNull(mTimeSyncSntpClient);
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeSyncSntpClient.DATE_FORMAT, Locale.ENGLISH);
        Date date = mTimeSyncSntpClient.getUTCTime();
        Date d = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
        String str = sdf.format(date);
        String str1 = sdf.format(d);
        assertNotSame(str, str1);
    }

    @Test
    public void testNtpHostsFromAppConfigJson() throws IllegalArgumentException {
        try {
            AppConfigurationInterface mConfigInterface = new AppConfigurationManager(mAppInfra) {
                @Override
                protected JSONObject getMasterConfigFromApp() {
                    JSONObject result = null;
                    try {
                        String testJson = ConfigValues.testJson();
                        result = new JSONObject(testJson);
                    } catch (Exception ignored) {
                    }
                    return result;
                }
            };
            assertNotNull(mConfigInterface);

            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();

            assertNotNull(mConfigInterface.getPropertyForKey
                    ("TIMESYNC.NTP.HOSTS", "APPINFRA", configError));

        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testisSynchronized() {
        if (mTimeSyncSntpClient.isSynchronized()) {
            assertTrue(mTimeSyncSntpClient.isSynchronized());
        } else {
            assertFalse(mTimeSyncSntpClient.isSynchronized());
        }
    }
}
