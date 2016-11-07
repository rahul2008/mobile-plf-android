package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.android.volley.Cache;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import java.io.File;

/**
 * Created by 310243577 on 11/7/2016.
 */

public class DiskCacheTest extends MockitoTestCase {

    private AppInfra mAppInfra;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
    }

    public void testInitialize(){
        mAppInfra = new AppInfra.Builder().build(context);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024, mAppInfra); //
        assertNotNull(cache);
        cache.initialize();
        Cache.Entry e = new Cache.Entry();
        e.data="sample data".getBytes();
        cache.put("key",e);
        assertNotNull(cache.get("key").data);
        cache.clear();
        cache.remove("key");
        assertNull(cache.get("key"));
        //assertEquals(e.data,cache.get("key").data);
    }

    private File getCacheDir(){
        return  mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
    }
}
