package com.philips.cdp.registration.coppa.utils;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Before;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class CoppaConstantsTest extends RegistrationApiInstrumentationBase {

    CoppaConstants coppaConstants;

    @Before
    public void setUp() throws Exception {
    super.setUp();
        coppaConstants= new CoppaConstants();
    }
    public void testLanuchFragment(){
        assertNotNull(coppaConstants);
        assertEquals("launchParentalFragment",coppaConstants.LAUNCH_PARENTAL_FRAGMENT);
    }
}