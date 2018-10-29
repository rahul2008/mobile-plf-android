/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DiskCacheTest {

    private AppInfra mAppInfra;
    private Context context;

    @Before
    protected void setUp() throws Exception {
        VolleyLog.DEBUG = false;
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
    }

    @Test
    public void testInitialize() {
        mAppInfra = new AppInfra.Builder().build(context);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024, mAppInfra); //

        assertNotNull(cache);

        cache.initialize();
        Cache.Entry e = new Cache.Entry();
        e.data = "sample data".getBytes();
        cache.put("key", e);
        cache.clear();
        cache.remove("key");

        assertNull(cache.get("key"));
    }

    private File getCacheDir() {
        return mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }
}
