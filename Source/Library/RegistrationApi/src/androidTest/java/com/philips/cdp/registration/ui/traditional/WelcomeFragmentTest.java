package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WelcomeFragmentTest extends RegistrationApiInstrumentationBase {
    WelcomeFragment welcomeFragment;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        welcomeFragment= new WelcomeFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(welcomeFragment);
    }
}