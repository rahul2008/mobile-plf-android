package com.philips.cdp.registration.errormapping;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/10/2016.
 */
public class CheckLocaleTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    CheckLocale checkLocale = new CheckLocale();


    public CheckLocaleTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    public void testCheckLanguage(){
        assertEquals("en-IN",checkLocale.checkLanguage("en-IN"));
        assertEquals("en-US",checkLocale.checkLanguage("abcd"));

    }

}