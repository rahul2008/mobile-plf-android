package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XPasswordHintTest extends InstrumentationTestCase{

    XPassword xPassword;
    @Before
    public void setUp() throws Exception {
        xPassword= new XPassword(getInstrumentation().getContext());
    }
    @Test
    public void testassert(){
        assertNotNull(xPassword);
    }
}