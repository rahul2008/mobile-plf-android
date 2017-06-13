
package com.philips.dhpclient.util;

import android.support.multidex.MultiDex;
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
        MultiDex.install(getInstrumentation().getTargetContext());

        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        mHsdpLog = new HsdpLog();
    }

    public void testHsdpLog() {
        mHsdpLog.enableLogging();
       assertTrue(mHsdpLog.isLoggingEnabled());
        mHsdpLog.d("tag", "message");
        mHsdpLog.e("tag", "message");
        mHsdpLog.i("tag", "message");
        mHsdpLog.v("tag", "message");
        mHsdpLog.disableLogging();
        assertFalse(mHsdpLog.isLoggingEnabled());
        mHsdpLog.d("tag", "message");
        mHsdpLog.e("tag", "message");
        mHsdpLog.i("tag", "message");
        mHsdpLog.v("tag", "message");
        assertNotNull(mHsdpLog);
    }
}

