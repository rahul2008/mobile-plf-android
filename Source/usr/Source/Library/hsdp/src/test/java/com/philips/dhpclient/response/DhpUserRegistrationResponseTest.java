
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
public class DhpUserRegistrationResponseTest {

    private DhpUserRegistrationResponse mDhpUserRegistrationResponse;
    private DhpUserRegistrationResponse mDhpUserRegistrationResponse1;

    private Map<String, Object> mRawResponse = Collections.emptyMap();

    @Before
    public void setUp() throws Exception {
        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample", mRawResponse);
        mDhpUserRegistrationResponse1 = new DhpUserRegistrationResponse("sample", mRawResponse);
    }

    @Test
    public void testDhpUserRegistrationResponse() {
        assertNotNull(mDhpUserRegistrationResponse);
        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample", "sample", mRawResponse);
        assertNotNull(mDhpUserRegistrationResponse);
        assertEquals(mDhpUserRegistrationResponse, mDhpUserRegistrationResponse);
        assertNotEquals(mDhpUserRegistrationResponse, mDhpUserRegistrationResponse1);
        assertNotEquals(mDhpUserRegistrationResponse, mRawResponse);
    }
}
