package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XRegErrorTest extends InstrumentationTestCase{
    XRegError xRegError;
    @Before
    public void setUp() throws Exception {
        xRegError = new XRegError(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xRegError);
    }
}