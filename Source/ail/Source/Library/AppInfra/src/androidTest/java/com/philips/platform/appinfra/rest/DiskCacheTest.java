package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

import java.io.File;

/**
 * DiskCache Test class.
 */

public class DiskCacheTest extends AppInfraInstrumentation {

    private AppInfra mAppInfra;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        VolleyLog.DEBUG = false;
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
    }

    public void testInitialize() {
        mAppInfra = new AppInfra.Builder().build(context);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024, mAppInfra); //
        assertNotNull(cache);
        cache.initialize();
        Cache.Entry e = new Cache.Entry();
        e.data = "sample data".getBytes();
        cache.put("key", e);
       // assertNotNull(cache.get("key").data);
     //   assertEquals(e.data, cache.get("key").data);
        cache.clear();
        cache.remove("key");
        assertNull(cache.get("key"));
    }

    private File getCacheDir() {
        return mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }
}
