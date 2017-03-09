package com.philips.cdp.registration.settings;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * Created by 310243576 on 9/16/2016.
 */
public class RegistrationSettingsURLTest extends InstrumentationTestCase{

    RegistrationSettingsURL registrationSettingsURL;
    Context mContext;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        registrationSettingsURL = new RegistrationSettingsURL();
        mContext = getInstrumentation().getTargetContext();
    }


}