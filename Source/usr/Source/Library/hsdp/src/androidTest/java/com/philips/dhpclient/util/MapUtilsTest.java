
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
import static org.junit.Assert.assertNull;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class MapUtilsTest {

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    @Test
    public void testExtract() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "aaaa");

        assertNull(MapUtils.extract(map, "helo"));
    }
}
