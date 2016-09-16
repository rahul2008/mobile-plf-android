package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XTextViewTest extends InstrumentationTestCase{
    XTextView xTextView;
    @Before
    public void setUp() throws Exception {
        xTextView= new XTextView(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xTextView);
    }
}