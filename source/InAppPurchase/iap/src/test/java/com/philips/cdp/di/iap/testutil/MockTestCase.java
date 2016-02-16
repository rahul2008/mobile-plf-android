package com.philips.cdp.di.iap.testutil;

import android.test.InstrumentationTestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class MockTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().
                getTargetContext().getCacheDir().getPath());
    }
}
