
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class HsdpLogTest {

    private HsdpLog mHsdpLog;

    @Before
    public void setUp() throws Exception {
        mHsdpLog = new HsdpLog();
    }

    @Test
    public void testHsdpLog() {
        HsdpLog.enableLogging();
        assertTrue(HsdpLog.isLoggingEnabled());

        HsdpLog.d("tag", "message");
        HsdpLog.e("tag", "message");
        HsdpLog.i("tag", "message");
        HsdpLog.v("tag", "message");
        HsdpLog.disableLogging();
        assertFalse(HsdpLog.isLoggingEnabled());

        HsdpLog.d("tag", "message");
        HsdpLog.e("tag", "message");
        HsdpLog.i("tag", "message");
        HsdpLog.v("tag", "message");

        assertNotNull(mHsdpLog);
    }
}

