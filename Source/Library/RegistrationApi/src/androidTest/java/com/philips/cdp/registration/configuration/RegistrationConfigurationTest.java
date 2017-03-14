package com.philips.cdp.registration.configuration;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.ui.utils.RLog;

import org.junit.Before;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class RegistrationConfigurationTest extends InstrumentationTestCase {

    RegistrationConfiguration registrationConfiguration;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        RLog.init();
    }
}