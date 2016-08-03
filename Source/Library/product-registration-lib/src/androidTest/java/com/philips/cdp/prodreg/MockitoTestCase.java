package com.philips.cdp.prodreg;

import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

import org.mockito.MockitoAnnotations;

public class MockitoTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }
}

