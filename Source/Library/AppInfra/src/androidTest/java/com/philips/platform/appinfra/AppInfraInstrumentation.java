package com.philips.platform.appinfra;

import android.test.InstrumentationTestCase;

public class AppInfraInstrumentation extends InstrumentationTestCase{

    @Override
    protected void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
    }
}

