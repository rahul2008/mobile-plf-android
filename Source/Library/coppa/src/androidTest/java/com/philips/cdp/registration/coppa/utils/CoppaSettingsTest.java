package com.philips.cdp.registration.coppa.utils;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class CoppaSettingsTest extends InstrumentationTestCase{
    CoppaSettings mCoppaSettings;
    Context mContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mCoppaSettings = new CoppaSettings(mContext);
    }

    public void testCoppaSetting(){
        RegistrationBaseConfiguration registrationConfiguration = new RegistrationBaseConfiguration();
        mCoppaSettings.setRegistrationConfiguration(registrationConfiguration);
        assertNotNull(mCoppaSettings);
    }
}