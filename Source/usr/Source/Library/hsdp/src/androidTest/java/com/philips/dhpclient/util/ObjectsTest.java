
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.util;

import android.support.multidex.MultiDex;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class ObjectsTest {

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    @Test
    public void testHash() throws Exception {
        Map<String, String> a = new HashMap<String, String>();

        assertNotEquals(a.hashCode(), Objects.hash(a));
    }

    @Test
    public void testEquals() throws Exception {
        Map<String, String> a = new HashMap<String, String>();
        Map<String, String> b = new HashMap<String, String>();

        assertTrue(Objects.equals(a, b));
        a = null;
        assertFalse(Objects.equals(a, b));
        b = null;
        assertTrue(Objects.equals(a, b));
    }
}
