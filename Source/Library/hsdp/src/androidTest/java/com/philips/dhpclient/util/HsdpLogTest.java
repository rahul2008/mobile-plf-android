package com.philips.dhpclient.util;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class HsdpLogTest extends InstrumentationTestCase {

    HsdpLog mHsdpLog;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mHsdpLog = new HsdpLog();
    }

    public void testHsdpLog() {
        mHsdpLog.enableLogging();
        mHsdpLog.disableLogging();
        mHsdpLog.isLoggingEnabled();
        mHsdpLog.d("tag", "message");
        mHsdpLog.e("tag", "message");
        mHsdpLog.i("tag", "message");
        mHsdpLog.v("tag", "message");
        assertNotNull(mHsdpLog);
    }
}