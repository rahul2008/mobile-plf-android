package com.philips.cdp.registration.ui.customviews;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XHavingProblemsTest extends InstrumentationTestCase{

    XHavingProblems xHavingProblems;
    @Before
    public void setUp() throws Exception {
        xHavingProblems= new XHavingProblems(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xHavingProblems);
    }
}