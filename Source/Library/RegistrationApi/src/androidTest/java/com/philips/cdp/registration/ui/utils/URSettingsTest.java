package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class URSettingsTest extends RegistrationApiInstrumentationBase {
    Context mContext;
    URSettings mURLaunchInput;

    @Before
    public void setUp() throws Exception {
           super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mURLaunchInput = new URSettings(mContext);
    }
    @Test
    public void testURSettng(){
        assertNotNull(mURLaunchInput);
    }
}