package com.philips.cdp.registration;


import android.support.multidex.MultiDex;

import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class RegistrationApiInstrumentationBase {
    @Before
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());

        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

    }

}
