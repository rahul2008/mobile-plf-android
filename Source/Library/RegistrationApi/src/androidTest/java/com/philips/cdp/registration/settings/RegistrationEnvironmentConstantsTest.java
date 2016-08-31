package com.philips.cdp.registration.settings;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/31/2016.
 */
public class RegistrationEnvironmentConstantsTest extends InstrumentationTestCase {

    RegistrationEnvironmentConstants registrationEnvironmentConstants;

    @Before
    public void setUp() throws Exception {
registrationEnvironmentConstants = new RegistrationEnvironmentConstants();
    }


    public void testEVAL(){
        assertEquals("Evaluation",RegistrationEnvironmentConstants.EVAL);
    }
    public void testDEV(){
        assertEquals("Development",RegistrationEnvironmentConstants.DEV);
    }

    public void testPROD(){
        assertEquals("Production",RegistrationEnvironmentConstants.PROD);
    }

    public void testTESTING(){
        assertEquals("Testing",RegistrationEnvironmentConstants.TESTING);
    }
    public void testSTAGING(){
        assertEquals("Staging",RegistrationEnvironmentConstants.STAGING);
    }


}