package com.philips.cdp.registration.coppa.utils;

import android.test.InstrumentationTestCase;

import org.junit.Before;
/**
 * Created by 310230979  on 8/31/2016.
 */
public class CoppaConstantsTest extends InstrumentationTestCase{

    CoppaConstants coppaConstants;

    @Before
    public void setUp() throws Exception {
        coppaConstants= new CoppaConstants();
    }
    public void testLanuchFragment(){
        assertNotNull(coppaConstants);
        assertEquals("launchParentalFragment",coppaConstants.LAUNCH_PARENTAL_FRAGMENT);
    }
}