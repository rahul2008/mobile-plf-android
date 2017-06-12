//package com.philips.dhpclient.util;
//
//import android.test.InstrumentationTestCase;
//import android.util.Log;
//
//import com.philips.dhpclient.HSDPInstrumentationBase;
//
//import org.junit.Before;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.*;
//
//
//public class HsdpLogTest extends HSDPInstrumentationBase {
//
//    HsdpLog mHsdpLog;
//
//    @Before
//    public void setUp() throws Exception {
//              super.setUp();
//        mHsdpLog = new HsdpLog();
//    }
//
//    public void testHsdpLog() {
//        mHsdpLog.enableLogging();
//       assertTrue(mHsdpLog.isLoggingEnabled());
//        mHsdpLog.d("tag", "message");
//        mHsdpLog.e("tag", "message");
//        mHsdpLog.i("tag", "message");
//        mHsdpLog.v("tag", "message");
//        mHsdpLog.disableLogging();
//        assertFalse(mHsdpLog.isLoggingEnabled());
//        mHsdpLog.d("tag", "message");
//        mHsdpLog.e("tag", "message");
//        mHsdpLog.i("tag", "message");
//        mHsdpLog.v("tag", "message");
//        assertNotNull(mHsdpLog);
//    }
//}