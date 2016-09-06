package com.philips.cdp.registration.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.errormapping.CheckLocale;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class RegistrationSettingsTest extends InstrumentationTestCase {

    Context mContext;
    RegistrationSettings mRegistrationSettings;

    @Before
    public void setUp() throws Exception {
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



        mRegistrationSettings.onLocaleMatchRefreshed("locale") ;
        mRegistrationSettings.onLocaleMatchRefreshed(null);

        mRegistrationSettings.onErrorOccurredForLocaleMatch(LocaleMatchError.DEFAULT) ;
        mRegistrationSettings.onErrorOccurredForLocaleMatch(LocaleMatchError.SERVER_ERROR) ;
        mRegistrationSettings.onErrorOccurredForLocaleMatch(LocaleMatchError.INPUT_VALIDATION_ERROR) ;
        mRegistrationSettings.onErrorOccurredForLocaleMatch(LocaleMatchError.INVALID_INPUT_LOCALE) ;
        mRegistrationSettings.onErrorOccurredForLocaleMatch(LocaleMatchError.NOT_FOUND) ;

        LocaleMatchListener localeMatchListener = new LocaleMatchListener() {
            @Override
            public void onLocaleMatchRefreshed(String s) {

            }

            @Override
            public void onErrorOccurredForLocaleMatch(LocaleMatchError localeMatchError) {

            }
        };
        assertNull(mRegistrationSettings.getProductRegisterUrl());

        assertNull(mRegistrationSettings.getProductRegisterListUrl());

        assertNull(mRegistrationSettings.getPreferredCountryCode());

        assertNull(mRegistrationSettings.getPreferredLangCode());



        assertNull(mRegistrationSettings.getResendConsentUrl());

        assertNull(mRegistrationSettings.getmRegisterBaseCaptureUrl());

//        mRegistrationSettings.refreshLocale(localeMatchListener);

    }
}