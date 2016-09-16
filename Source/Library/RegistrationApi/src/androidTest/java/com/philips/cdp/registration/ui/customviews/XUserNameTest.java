package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XUserNameTest extends InstrumentationTestCase{
    XUserName xUserName;
    @Before
    public void setUp() throws Exception {
        xUserName= new XUserName(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xUserName);
    }
}