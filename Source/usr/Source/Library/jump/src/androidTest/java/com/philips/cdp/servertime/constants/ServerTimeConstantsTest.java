package com.philips.cdp.servertime.constants;

import android.test.InstrumentationTestCase;

import com.philips.ntputils.constants.ServerTimeConstants;

public class ServerTimeConstantsTest extends InstrumentationTestCase {

    ServerTimeConstants mServerTimeConstants;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mServerTimeConstants = new ServerTimeConstants();
    }

    public void testOffset() throws Exception {
        assertEquals("offset", mServerTimeConstants.OFFSET);
    }

    public void testoffsetElapsed() throws Exception {
        assertEquals("offsetElapsed", mServerTimeConstants.OFFSET_ELAPSED);
    }

    public void testYear() throws Exception {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", mServerTimeConstants.DATE_FORMAT);
    }

    public void testMonth() throws Exception {
        assertEquals("yyyy-MM-dd HH:mm:ss Z", mServerTimeConstants.DATE_FORMAT_COPPA);
    }

    public void testDay() throws Exception {
        assertEquals("yyyy-MM-dd HH:mm:ss", mServerTimeConstants.DATE_FORMAT_FOR_JUMP);
    }

    public void testUtc() throws Exception {
        assertEquals("UTC", mServerTimeConstants.UTC);
    }

    public void testNUmber() throws Exception {
        assertEquals(4, mServerTimeConstants.POOL_SIZE);
    }
}
