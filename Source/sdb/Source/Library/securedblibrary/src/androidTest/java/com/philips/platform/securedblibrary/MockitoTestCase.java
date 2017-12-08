package com.philips.platform.securedblibrary;

import android.test.InstrumentationTestCase;

import org.mockito.MockitoAnnotations;

public class MockitoTestCase extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
    }
}

