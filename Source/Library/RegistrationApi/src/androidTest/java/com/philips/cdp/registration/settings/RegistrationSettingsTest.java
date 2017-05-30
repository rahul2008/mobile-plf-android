package com.philips.cdp.registration.settings;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;


import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class RegistrationSettingsTest extends InstrumentationTestCase {

    Context mContext;
    RegistrationSettings mRegistrationSettings;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
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