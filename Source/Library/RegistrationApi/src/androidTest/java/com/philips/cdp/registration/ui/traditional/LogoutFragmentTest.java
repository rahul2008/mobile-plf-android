package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LogoutFragmentTest extends InstrumentationTestCase{

    LogoutFragment  logoutFragment;

    @Before
    public void setUp() throws Exception {
        logoutFragment=new LogoutFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(logoutFragment);
    }
}
