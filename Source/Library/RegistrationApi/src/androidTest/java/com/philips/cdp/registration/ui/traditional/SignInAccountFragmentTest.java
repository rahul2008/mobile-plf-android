package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SignInAccountFragmentTest extends InstrumentationTestCase{

    SignInAccountFragment signInAccountFragment;
    @Before
    public void setUp() throws Exception {
        signInAccountFragment= new SignInAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(signInAccountFragment);
    }
}