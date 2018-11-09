package com.philips.cdp.registration.hsdp;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HsdpResponseTest extends TestCase {
    private HsdpResponse mDhpResponse;
    private Map<String, Object> mRawResponse;

    @Before
    public void setUp() throws Exception {
        mRawResponse = new HashMap<>();
        mDhpResponse = new HsdpResponse(mRawResponse);
    }

    @Test
    public void testDhpResponse() {
        assertNotNull(mDhpResponse);
        mDhpResponse = new HsdpResponse(mRawResponse);
        assertNotNull(mDhpResponse);
        assertEquals(mDhpResponse, mDhpResponse);
        assertNotEquals(mDhpResponse, mRawResponse);
        assertNotNull(mDhpResponse.toString());
    }

}