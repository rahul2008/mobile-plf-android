package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegPreferenceUtilityTest extends RegistrationApiInstrumentationBase {

    RegPreferenceUtility regPreferenceUtility;
    @Before
    public void setUp() throws Exception {
       super.setUp();
        regPreferenceUtility= new RegPreferenceUtility();
    }
    @Test
    public void testAssert(){
        assertNotNull(regPreferenceUtility);
    }
    @Test
    public void testStorePreference(){
        regPreferenceUtility.storePreference(getInstrumentation().getContext(),"REGAPI_PREFERENCE",true);
    }
    @Test
    public void testGetStoredState(){
        regPreferenceUtility.getStoredState(getInstrumentation().getContext(),"REGAPI_PREFERENCE");
    }


}