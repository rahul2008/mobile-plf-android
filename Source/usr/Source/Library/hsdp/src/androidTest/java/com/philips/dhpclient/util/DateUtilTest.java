/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.util;

import android.support.multidex.MultiDex;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotSame;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class DateUtilTest {

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    @Test
    public void testGetTimestamp() throws Exception {
        assertNotSame("dd", DateUtil.getTimestamp());
    }
}
