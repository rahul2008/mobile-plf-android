package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WelcomeFragmentTest extends InstrumentationTestCase{
    WelcomeFragment welcomeFragment;
    @Before
    public void setUp() throws Exception {
        welcomeFragment= new WelcomeFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(welcomeFragment);
    }
}