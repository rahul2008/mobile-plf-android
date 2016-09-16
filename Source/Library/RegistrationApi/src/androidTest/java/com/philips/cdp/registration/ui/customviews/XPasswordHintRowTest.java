package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XPasswordHintRowTest extends InstrumentationTestCase{
    XPasswordHintRow xPasswordHintRow;
    @Before
    public void setUp() throws Exception {
        xPasswordHintRow= new XPasswordHintRow(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xPasswordHintRow);
    }
}