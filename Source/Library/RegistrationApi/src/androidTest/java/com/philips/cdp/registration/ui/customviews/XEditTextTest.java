package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XEditTextTest extends InstrumentationTestCase{

    XEditText xEditText;
    @Before
    public void setUp() throws Exception {
        xEditText= new XEditText(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xEditText);
    }
}