/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.response;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpResponseTest {

    private DhpResponse mDhpResponse;
    private DhpResponse mDhpResponse1;

    private Map<String, Object> mRawResponse;

    @Before
    public void setUp() throws Exception {
        mRawResponse = new HashMap<>();
        mDhpResponse = new DhpResponse(mRawResponse);
        mDhpResponse1 = new DhpResponse(mRawResponse);

    }

    @Test
    public void testDhpResponse() {
        assertNotNull(mDhpResponse);
        mDhpResponse = new DhpResponse("sample", mRawResponse);
        assertNotNull(mDhpResponse);
        assertEquals(mDhpResponse, mDhpResponse);
        assertNotEquals(mDhpResponse, mDhpResponse1);
        assertNotEquals(mDhpResponse, mRawResponse);
        assertNotNull(mDhpResponse.toString());
    }
}
