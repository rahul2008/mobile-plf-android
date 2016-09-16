package com.philips.cdp.registration.ui.utils;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegPreferenceUtilityTest extends InstrumentationTestCase {

    RegPreferenceUtility regPreferenceUtility;
    @Before
    public void setUp() throws Exception {
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