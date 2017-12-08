package com.philips.cdp.registration.settings;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;


public class RegistrationSettingsTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    RegistrationSettings mRegistrationSettings;

    @Before
    public void setUp() throws Exception {
        super.setUp();
          mContext =  getInstrumentation()
                .getTargetContext();
        mRegistrationSettings = new RegistrationSettings() {
            @Override
            public void initialiseConfigParameters(String locale) {

            }
        };
    }
    public void testRegistrationSettings(){
//        mRegistrationSettings.intializeRegistrationSettings(mContext, "captureClientId",
//                "locale");

        mRegistrationSettings.initialiseConfigParameters("locale");
        mRegistrationSettings.initialiseConfigParameters(null);


        assertNull(mRegistrationSettings.getProductRegisterUrl());

        assertNull(mRegistrationSettings.getProductRegisterListUrl());

        assertNull(mRegistrationSettings.getPreferredCountryCode());

        assertNull(mRegistrationSettings.getPreferredLangCode());



        assertNull(mRegistrationSettings.getResendConsentUrl());

        assertNull(mRegistrationSettings.getmRegisterBaseCaptureUrl());

//        mRegistrationSettings.refreshLocale(localeMatchListener);

    }

    @Test
    public void testassignLanguageAndCountryCode(){
        String locale="en_US";

        String localeArr[] = locale.split("_");
        assertNotNull(localeArr);
        String mCountryCode;

        String mLanguageCode;
        mLanguageCode = localeArr[0].toLowerCase();
        mCountryCode = localeArr[1].toUpperCase();
        assertEquals("en",mLanguageCode);
        assertEquals("US",mCountryCode);

    }
}