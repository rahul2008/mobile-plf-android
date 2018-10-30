
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpCommunicationExceptionTest {

    private DhpCommunicationException mDhpCommunicationException;

    @Before
    public void setUp() throws Exception {
        Throwable e = new Throwable();
        mDhpCommunicationException = new DhpCommunicationException(e);
    }

    @Test
    public void testDhpcommunicationException() {
        assertNotNull(mDhpCommunicationException);
        mDhpCommunicationException = new DhpCommunicationException("sample", "sample");
        assertNotNull(mDhpCommunicationException);
    }
}
