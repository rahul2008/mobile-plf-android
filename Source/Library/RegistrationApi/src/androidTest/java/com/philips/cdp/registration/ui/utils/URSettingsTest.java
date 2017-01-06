package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class URSettingsTest extends InstrumentationTestCase {
    Context mContext;
    URSettings mURLaunchInput;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mURLaunchInput = new URSettings(mContext);
    }
    public void testURSettng(){
        assertNotNull(mURLaunchInput);
    }
}