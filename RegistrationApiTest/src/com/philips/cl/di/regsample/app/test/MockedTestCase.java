package com.philips.cl.di.regsample.app.test;

import android.test.InstrumentationTestCase;

public abstract class MockedTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().
        		getTargetContext().getCacheDir().getPath());
    }
}