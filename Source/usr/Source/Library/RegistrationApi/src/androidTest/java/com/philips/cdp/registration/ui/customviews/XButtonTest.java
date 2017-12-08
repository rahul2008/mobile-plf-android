package com.philips.cdp.registration.ui.customviews;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XButtonTest extends RegistrationApiInstrumentationBase {

    XButton xButton;

    @Before
    public void setUp() throws Exception {
       super.setUp();
        xButton = new XButton(getInstrumentation().getContext());
    }

    @Test
    public void testSetTypeface() throws Exception {
        assertNotNull(xButton);
    }
}