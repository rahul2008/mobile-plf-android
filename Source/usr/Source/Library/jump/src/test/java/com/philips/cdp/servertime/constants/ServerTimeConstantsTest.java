package com.philips.cdp.servertime.constants;

import com.philips.ntputils.constants.ServerTimeConstants;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServerTimeConstantsTest extends TestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testOffset() {
        assertEquals("offset", ServerTimeConstants.OFFSET);
    }

    @Test
    public void testoffsetElapsed() {
        assertEquals("offsetElapsed", ServerTimeConstants.OFFSET_ELAPSED);
    }

    @Test
    public void testYear() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", ServerTimeConstants.DATE_FORMAT);
    }

    @Test
    public void testMonth() {
        assertEquals("yyyy-MM-dd HH:mm:ss Z", ServerTimeConstants.DATE_FORMAT_COPPA);
    }

    @Test
    public void testDay() {
        assertEquals("yyyy-MM-dd HH:mm:ss", ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
    }

    @Test
    public void testUtc() {
        assertEquals("UTC", ServerTimeConstants.UTC);
    }

    @Test
    public void testNUmber() {
        assertEquals(4, ServerTimeConstants.POOL_SIZE);
    }
}
