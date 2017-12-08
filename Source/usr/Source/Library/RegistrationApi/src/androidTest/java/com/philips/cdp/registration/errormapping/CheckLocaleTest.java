package com.philips.cdp.registration.errormapping;

import android.support.multidex.MultiDex;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;


public class CheckLocaleTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    CheckLocale checkLocale = new CheckLocale();


    public CheckLocaleTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    public void testCheckLanguage(){
        assertEquals("en-IN",checkLocale.checkLanguage("en-IN"));
        assertEquals("en-US",checkLocale.checkLanguage("abcd"));

    }

}