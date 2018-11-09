package com.philips.cdp.registration.hsdp;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;

public class HsdpAuthenticationResponseTest extends TestCase {
    private HsdpAuthenticationResponse mDhpAuthenticationResponse;
    private HsdpAuthenticationResponse mDhpAuthenticationResponse1;

    private Map<String, Object> mRawResponse = Collections.emptyMap();

    @Before
    public void setUp() throws Exception {
        mDhpAuthenticationResponse = new HsdpAuthenticationResponse(mRawResponse);
        mDhpAuthenticationResponse1 = new HsdpAuthenticationResponse(mRawResponse);
    }

    @Test
    public void testDhpAuthenticationResponse() {
        assertNotNull(mDhpAuthenticationResponse);
        mDhpAuthenticationResponse = new HsdpAuthenticationResponse("accessToken", "refreshToken", 1221, "userId", mRawResponse);
        assertNotNull(mDhpAuthenticationResponse);
        assertEquals(mDhpAuthenticationResponse, mDhpAuthenticationResponse);
        assertNotEquals(mDhpAuthenticationResponse, mDhpAuthenticationResponse1);
        assertNotEquals(mDhpAuthenticationResponse, mRawResponse);
        assertNotNull(mDhpAuthenticationResponse.toString());
    }
}