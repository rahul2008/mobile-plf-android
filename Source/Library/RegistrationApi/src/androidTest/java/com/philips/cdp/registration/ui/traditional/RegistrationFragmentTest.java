package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationFragmentTest extends InstrumentationTestCase{
    RegistrationFragment registrationFragment;
    @Before
    public void setUp() throws Exception {
        registrationFragment= new RegistrationFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(registrationFragment);
    }
}