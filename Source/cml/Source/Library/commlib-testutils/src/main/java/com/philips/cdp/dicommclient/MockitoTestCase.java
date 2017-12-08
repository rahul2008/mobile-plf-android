package com.philips.cdp.dicommclient;

import android.test.InstrumentationTestCase;

import org.mockito.MockitoAnnotations;

@Deprecated
public class MockitoTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }
}
