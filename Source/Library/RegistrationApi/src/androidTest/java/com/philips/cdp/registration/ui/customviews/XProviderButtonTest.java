package com.philips.cdp.registration.ui.customviews;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XProviderButtonTest extends InstrumentationTestCase{
    XProviderButton xProviderButton ;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        xProviderButton= new XProviderButton(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xProviderButton);
    }
}