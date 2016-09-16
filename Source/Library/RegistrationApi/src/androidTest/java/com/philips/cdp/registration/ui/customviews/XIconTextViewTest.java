package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XIconTextViewTest extends InstrumentationTestCase{
    XIconTextView xIconTextView;
    @Before
    public void setUp() throws Exception {
        xIconTextView= new XIconTextView(getInstrumentation().getContext());
    }
    @Test
    public void testassert(){
        assertNotNull(xIconTextView);
    }
}