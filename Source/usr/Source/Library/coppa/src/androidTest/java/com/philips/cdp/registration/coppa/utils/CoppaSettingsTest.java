package com.philips.cdp.registration.coppa.utils;

import android.content.Context;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Before;

import static junit.framework.Assert.assertNotNull;


public class CoppaSettingsTest extends RegistrationApiInstrumentationBase {
    CoppaSettings mCoppaSettings;
    Context mContext;

    @Before
    public void setUp() throws Exception {

        super.setUp();

        mCoppaSettings = new CoppaSettings(mContext);
    }

    public void testCoppaSetting(){
     //   RegistrationBaseConfiguration registrationConfiguration = new RegistrationBaseConfiguration();
       // mCoppaSettings.setRegistrationConfiguration(registrationConfiguration);
        assertNotNull(mCoppaSettings);
    }
}