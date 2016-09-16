package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XErrorAlertIconTest extends InstrumentationTestCase{
    XErrorAlertIcon xErrorAlertIcon;
    @Before
    public void setUp() throws Exception {
        xErrorAlertIcon= new XErrorAlertIcon(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xErrorAlertIcon);
    }
}