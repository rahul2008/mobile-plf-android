
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.response;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpAuthenticationResponseTest {
    private DhpAuthenticationResponse mDhpAuthenticationResponse;
    private DhpAuthenticationResponse mDhpAuthenticationResponse1;

    private Map<String, Object> mRawResponse = Collections.emptyMap();

    @Before
    public void setUp() throws Exception {
        mDhpAuthenticationResponse = new DhpAuthenticationResponse(mRawResponse);
        mDhpAuthenticationResponse1 = new DhpAuthenticationResponse(mRawResponse);
    }

    @Test
    public void testDhpAuthenticationResponse() {
        assertNotNull(mDhpAuthenticationResponse);
        mDhpAuthenticationResponse = new DhpAuthenticationResponse("accessToken", "refreshToken", 1221, "userId", mRawResponse);
        assertNotNull(mDhpAuthenticationResponse);
        assertEquals(mDhpAuthenticationResponse, mDhpAuthenticationResponse);
        assertNotEquals(mDhpAuthenticationResponse, mDhpAuthenticationResponse1);
        assertNotEquals(mDhpAuthenticationResponse, mRawResponse);
        assertNotNull(mDhpAuthenticationResponse.toString());
    }
}
