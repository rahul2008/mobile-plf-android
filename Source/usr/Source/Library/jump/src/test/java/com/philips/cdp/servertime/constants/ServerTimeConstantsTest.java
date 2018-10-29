/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.servertime.constants;

import com.philips.ntputils.constants.ServerTimeConstants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerTimeConstantsTest {

    @Test
    public void testOffset() throws Exception {
        assertEquals("offset", ServerTimeConstants.OFFSET);
    }

    @Test
    public void testoffsetElapsed() throws Exception {
        assertEquals("offsetElapsed", ServerTimeConstants.OFFSET_ELAPSED);
    }

    @Test
    public void testYear() throws Exception {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", ServerTimeConstants.DATE_FORMAT);
    }

    @Test
    public void testMonth() throws Exception {
        assertEquals("yyyy-MM-dd HH:mm:ss Z", ServerTimeConstants.DATE_FORMAT_COPPA);
    }

    @Test
    public void testDay() throws Exception {
        assertEquals("yyyy-MM-dd HH:mm:ss", ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
    }

    @Test
    public void testUtc() throws Exception {
        assertEquals("UTC", ServerTimeConstants.UTC);
    }

    @Test
    public void testNumber() throws Exception {
        assertEquals(4, ServerTimeConstants.POOL_SIZE);
    }
}
