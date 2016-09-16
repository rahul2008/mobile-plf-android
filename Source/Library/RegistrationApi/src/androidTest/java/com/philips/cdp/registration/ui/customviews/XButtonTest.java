package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XButtonTest extends InstrumentationTestCase {

    XButton xButton;

    @Before
    public void setUp() throws Exception {
        xButton = new XButton(getInstrumentation().getContext());
    }

    @Test
    public void testSetTypeface() throws Exception {
        assertNotNull(xButton);
    }
}