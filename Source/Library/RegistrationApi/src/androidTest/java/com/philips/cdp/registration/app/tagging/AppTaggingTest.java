package com.philips.cdp.registration.app.tagging;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * Created by 310243576 on 8/30/2016.
 */
public class AppTaggingTest extends InstrumentationTestCase{

    Context mContext;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
    }
}